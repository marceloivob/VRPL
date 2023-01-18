import { PoModel, EventoModel, FrenteDeObraModel, MacroServicoCFFDTOModel,
         ServicoCadastroModel, EventoCFFcomEventosModel, PoDetalhadaModel } from './po-cff.state.model';

export class LoadPoCffLicitacao {
    static readonly type = '[VRPL]LOAD_PO_CFF_LICITACAO';

    constructor(public idLicitacao: number) { }

}

export class SavePoDadosGerais {
    static readonly type = '[VRPL]SAVE_DADOS_GERAIS';

    constructor(
        public po: PoModel
    ) { }
}

export class ValidarDataBasePo {
    static readonly type = '[VRPL]VALIDAR_DATA_BASE_PO';

    constructor(
        public po: PoModel
    ) { }
}


export class SaveEvento {
    static readonly type = '[VRPL]SAVE_EVENTO_PO';

    constructor(
        public idPO: number,
        public evento: EventoModel
    ) { }
}

export class DeleteEvento {
    static readonly type = '[VRPL]DELETE_EVENTO_PO';

    constructor(
        public idPO: number,
        public evento: EventoModel
    ) { }
}

export class LoadEventosPO {
    static readonly type = '[VRPL]LOAD_EVENTOS_PO';
    constructor( ) { }
}

export class LoadPoDetalhada {
    static readonly type = '[VRPL]LOAD_PO_DETALHADA';

    constructor(
        public idPo: number,
        public apenasVisualizar = false
    ) { }
}

export class LoadFrentesDeObraPO {
    static readonly type = '[VRPL]LOAD_FRENTES_DE_OBRA_PO';
    constructor( ) { }
}

export class SaveFrenteDeObra {
    static readonly type = '[VRPL]SAVE_FRENTE_DE_OBRA_PO';

    constructor(
        public idPO: number,
        public frenteDeObra: FrenteDeObraModel
    ) { }
}

export class DeleteFrenteDeObra {
    static readonly type = '[VRPL]DELETE_FRENTE_DE_OBRA_PO';

    constructor(
        public idPO: number,
        public frenteDeObra: FrenteDeObraModel
    ) { }
}


export class LoadCffSemEventosPO {
    static readonly type = '[VRPL]LOAD_CFF_SEM_EVENTOS_PO';
    constructor( ) { }
}

export class LoadCffComEventosPO {
    static readonly type = '[VRPL]LOAD_CFF_COM_EVENTOS_PO';
    constructor( ) { }
}

export class LoadMacrosservicosPO {
    static readonly type = '[VRPL]LOAD_MACROSSERVICOS_PO';
    constructor( ) { }
}

export class LoadEventosServico {
    static readonly type = '[VRPL]LOAD_EVENTOS_EVENTOS';
    constructor(
        public macrosservicoId: number,
        public servicoId: number
    ) { }
}

export class LoadFrentesDeObraServico {
    static readonly type = '[VRPL]LOAD_FRENTES_DE_OBRA';
    constructor(
        public macrosservicoId: number,
        public servicoId: number
    ) { }
}

export class LoadFrentesDeObraServicoAnalise {
    static readonly type = '[VRPL]LOAD_FRENTES_DE_OBRA_SERVICO_ANALISE';
    constructor(
        public servicoId: number
    ) { }
}

export class SaveCFFSemEventos {
    static readonly type = '[VRPL]SAVE_CFF_SEM_EVENTOS';

    constructor(
        public idPO: number,
        public macroServico: MacroServicoCFFDTOModel
    ) { }
}

export class SaveServicoPo {
    static readonly type = '[VRPL]SAVE_SERVICO_PO';

    constructor(
        public servico: ServicoCadastroModel
    ) { }
}

export class SaveCFFComEventos {
    static readonly type = '[VRPL]SAVE_CFF_COM_EVENTOS';

    constructor(
        public idPO: number,
        public idEvento: number,
        public eventoCff: EventoCFFcomEventosModel
    ) { }
}

export class UpdateReferenciaPo {
    static readonly type = '[VRPL]UPDATE_REFERENCIA_PO';

    constructor(
        public idPO: number,
        public referencia: string,
        public versao: number
    ) { }
}

export class LoadPLQ {
    static readonly type = '[VRPL]LOAD_PLQ';
    constructor( ) { }
}
