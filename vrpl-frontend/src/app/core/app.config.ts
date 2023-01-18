

export enum Stage {
  LOCAL, DEVELOPMENT, TEST, INTEGRATION_TEST, ACCEPTANCE, PRODUCTION
}

interface ServerConfig {
  IDP: string;
  SICONV: string;
}

export class AppConfig {

    private static _stage = Stage.LOCAL;
    private static _sentryDsn =  '';

    private static _urlToIDPService: string;
    private static _urlToSICONVService: string;
    private static _idpAppName: string;

    private static _loaded = false;

    static readonly isLocalEnvironment = (window.location.hostname.search('localhost') >= 0);

    public static get endpoint(): string {
        if (AppConfig.isLocalEnvironment) {
            return 'http://localhost:8080';
        } else {
            return window.location.origin + '/siconv-vrpl-backend';
        }
    }

    public static get stage() {
      return AppConfig._stage;
    }

    public static get sentryDsn() {
      return AppConfig._sentryDsn;
    }

    public static get urlToIDPService() {
      return AppConfig._urlToIDPService;
    }
    public static get urlToSICONVService() {
      return AppConfig._urlToSICONVService;
    }

    public static get idpAppName() {
      return AppConfig._idpAppName;
    }

    public static get loaded() {
      return AppConfig._loaded;
    }

    public static loadSettings(): Promise<any> {
      return fetch(`${AppConfig.endpoint}/app/integrations`)
        .then( (response) => response.json() )
        .then( (parsedResponse) => {
          const config: ServerConfig = parsedResponse.data;
          AppConfig._urlToSICONVService = config.SICONV;
          AppConfig._urlToIDPService = config.IDP;

          if (AppConfig.isLocalEnvironment) {
            AppConfig._idpAppName = 'VRPLD';
          } else {
            AppConfig._idpAppName = 'VRPL';
          }

          AppConfig._loaded = true;
        }).catch( (e) => console.error('Não foi possível obter configuração do Servidor!', e) );
    }
}
