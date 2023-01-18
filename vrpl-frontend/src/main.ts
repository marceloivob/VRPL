import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';

import * as Sentry from '@sentry/browser';
import { AppConfig } from './app/core/app.config';

if (!AppConfig.isLocalEnvironment) {
  enableProdMode();
  // Comentário temporário enquanto não resolve a questão do Endpoint de recebimento das mensagens do Sentry
  // Sentry.init({
  //   dsn: 'https://e694b582b1124345aab680447c9a8156@sentry.estaleiro.serpro.gov.br/127'
  // });
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));

