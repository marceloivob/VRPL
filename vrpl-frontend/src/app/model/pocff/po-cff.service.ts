import { map } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Store } from '@ngxs/store';
import { Observable } from 'rxjs';
import { PoModel, EventoModel, PoDetalhadaModel, FrenteDeObraModel, VisaoParcelasPorMacroServicoModel,
     MacrosservicoModel, CFFSemEventosDTOModel,
     FrenteDeObraQuantidadeModel,
     ParcelaCffSemEventosModel,
     MacroServicoCFFDTOModel,
     ServicoCadastroModel,
     CFFComEventosModel,
     EventoCFFcomEventosModel,
     ServicoFrenteDeObraAnaliseModel,
     PLQModel, PoMacrosservicoServicoModel} from './po-cff.state.model';
import { AppConfig } from 'src/app/core/app.config';

@Injectable({
    providedIn: 'root'
})
export class PoCffService {

    constructor(private readonly http: HttpClient) { }

    saveEvento(idPO: number, evento: EventoModel): any {
        const url = `${AppConfig.endpoint}/po/${idPO}/evento`;

        return this.http.post<{ status: string }>(url, evento);
    }

    carregarListaPoCff(idLicitacao: number): Observable<PoModel[]> {
        return this.http
            .get<any[]>(`${AppConfig.endpoint}/po?idLicitacao=${idLicitacao}`)
            .pipe(
                map((posApi) => {
                    const pos: PoModel[] = posApi.map(poApi => {
                        console.log(JSON.stringify(poApi));
                        return {
                            id: poApi.id,
                            idAnalise: poApi.idAnalise,
                            dataBaseAnalise: poApi.dataBaseAnalise,
                            dataBaseVrpl: poApi.dataBaseVrpl,
                            localidade: poApi.siglaLocalidade,
                            previsaoAnalise: poApi.dataPrevisaoInicioObraAnalise,
                            previsaoInicioVRPL: poApi.dataPrevisaoInicioObra,
                            duracao: poApi.qtMesesDuracaoObra,
                            qtMesesDuracaoObraValorOriginal : poApi.qtMesesDuracaoObraValorOriginal,
                            acompanhamentoEvento: poApi.indicadorAcompanhamentoPorEvento,
                            precoTotalAnalise: poApi.precoTotalAnalise,
                            precoTotalLicitacao: poApi.precoTotalLicitacao,
                            descricaoMeta : poApi.descricaoMeta,
                            numeroMeta : poApi.numeroMeta,
                            numeroSubmeta: poApi.numeroSubmeta,
                            descricao: poApi.descricaoSubmeta,
                            inTrabalhoSocial: poApi.inTrabalhoSocial,
                            versao: poApi.versao
                        };
                    });

                    return pos;
                })
            );
    }

    salvar(po: PoDetalhadaModel) {
        const url = `${AppConfig.endpoint}/po`;
        return this.http.post<{ status: string }>(url, po);
    }

    validarDataBase(po: PoDetalhadaModel) {
        const url = `${AppConfig.endpoint}/po/database`;
        return this.http.post<{ status: string }>(url, po);
    }

    deleteEvento(idPo: number, evento: EventoModel) {
        return this.http
            .delete<{ status: string }>(`${AppConfig.endpoint}/po/${idPo}/evento/?idEvento=${evento.id}&versao=${evento.versao}`);
    }

    loadEventoById(idPo: number, idEvento: number): Observable<EventoModel> {
        return this.http.get<EventoModel>(`${AppConfig.endpoint}/po/${idPo}/evento/?idEvento=${idEvento}`);
    }

    recuperarSequencialEvento(idPo: number): Observable<number> {
        return this.http.get<number>(`${AppConfig.endpoint}/po/${idPo}/evento/sequencial`);
    }

    carregarPoDetalhada(idPo: number): Observable<PoDetalhadaModel> {
        return this.http.get<PoDetalhadaModel>(`${AppConfig.endpoint}/po/${idPo}`);
    }

    recuperarDadosDaListagem(idPo: number, idPoAnalise: number): Observable<EventoModel[]> {
        return this.http
            .get<any[]>(`${AppConfig.endpoint}/po/${idPo}/eventos/?idPOAnalise=${idPoAnalise}`)
            .pipe(
                map((anexosApi) => {
                    const eventos: EventoModel[] = anexosApi.map(eventoApi => {
                        return {
                            id: eventoApi.id,
                            tituloEvento: eventoApi.tituloEvento,
                            numeroEvento: eventoApi.numeroEvento,
                            versao: eventoApi.versao
                        };
                    });

                    return eventos;
                })
            );
    }

    recuperarFrentesDeObraDaListagem(idPo: number): Observable<FrenteDeObraModel[]> {
        return this.http
            .get<FrenteDeObraModel[]>(`${AppConfig.endpoint}/po/${idPo}/frentes`);

    }

    loadFrenteDeObraById(idPo: number, idFrenteDeObra: number): Observable<FrenteDeObraModel> {
        return this.http.get<FrenteDeObraModel>(`${AppConfig.endpoint}/po/${idPo}/frente/?idFrenteDeObra=${idFrenteDeObra}`);
    }

    recuperarSequencialFrenteDeObra(idPo: number): Observable<number> {
        return this.http.get<number>(`${AppConfig.endpoint}/po/${idPo}/frente/sequencial`);
    }

    saveFrenteDeObra(idPO: number, frente: FrenteDeObraModel): any {
        const url = `${AppConfig.endpoint}/po/${idPO}/frente`;

        return this.http.post<{ status: string }>(url, frente);
    }

    deleteFrenteDeObra(idPo: number, frente: FrenteDeObraModel) {
        return this.http
            .delete<{ status: string }>(`${AppConfig.endpoint}/po/${idPo}/frente/?idFrenteDeObra=${frente.id}&versao=${frente.versao}`);
    }

    recuperarCffSemEventosDaListagem(idPo: number, idPoAnalise: number): Observable<VisaoParcelasPorMacroServicoModel[]> {
        const cffDTOObservable: Observable<CFFSemEventosDTOModel> =
            this.http.get<CFFSemEventosDTOModel>(`${AppConfig.endpoint}/po/${idPo}/cffsemeventos`);

        return cffDTOObservable.pipe(
            map (cff => this.converterCffDTOVisaoParcelas(cff))
          );
    }

    saveCFFSemEventos(idPO: number, macroServico: MacroServicoCFFDTOModel): any {
        const url = `${AppConfig.endpoint}/po/${idPO}/cffsemeventos/${macroServico.id}`;
        return this.http.put<{ status: string }>(url, macroServico);
    }

    loadMacrosservicosPo(idPo: number, idPoAnalise: number): Observable<PoMacrosservicoServicoModel> {
        return this.http
            .get<any>(`${AppConfig.endpoint}/macroservico/po/${idPo}`)
            .pipe(
                map((api) => {
                    const poMacrosservicoServicos: PoMacrosservicoServicoModel = {
                        id: api.poId,
                        totalGeralLicitado: api.totalGeralLicitado,
                        totalGeralAceitoNaAnalise: api.totalGeralAceitoNaAnalise,
                        totalGeralNaDataBaseDaLicitacao: api.totalGeralNaDataBaseDaLicitacao,
                        macrosservicos: api.macroservicos.map(macrosservicoApi => {
                            return {
                                id: macrosservicoApi.macroServico.id,
                                item: macrosservicoApi.macroServico.nrMacroServico,
                                nome: macrosservicoApi.macroServico.txDescricao,
                                precoTotalLicitado: macrosservicoApi.precoTotalLicitado,
                                precoTotalAnalise: macrosservicoApi.precoTotalAnalise,
                                precoTotalAceitoNaAnalise: macrosservicoApi.precoTotalAceitoNaAnalise,
                                precoTotalNaDataBaseDaLicitacao: macrosservicoApi.precoTotalNaDataBaseDaLicitacao,
                                submetaId: macrosservicoApi.submetaId,
                                servicos: macrosservicoApi.servicos.map(servicoApi => {
                                    return {
                                        id: servicoApi.id,
                                        item: servicoApi.nrServico,
                                        fonte: servicoApi.inFonte,
                                        nome: servicoApi.txDescricao,
                                        eventoFK: servicoApi.eventoFk,
                                        codigo: servicoApi.cdServico,
                                        observacao: servicoApi.txObservacao,
                                        unidade: servicoApi.sgUnidade,
                                        bdi: servicoApi.pcBdi,
                                        bdiLicitado: servicoApi.pcBdiLicitado,
    
                                        precoTotalLicitado: servicoApi.vlPrecoTotalDaLicitacao,
                                        precoTotalAnalise: servicoApi.vlPrecoTotalAceitoNaAnalise,
                                        precoTotalDataBase: servicoApi.vlPrecoTotalDataBaseDaLicitacao,
    
                                        precoUnitarioLicitado: servicoApi.vlPrecoUnitarioDaLicitacao,
                                        precoUnitarioAnalise: servicoApi.vlPrecoUnitarioAceitoNaAnalise,
                                        precoUnitarioDataBase: servicoApi.vlPrecoUnitarioNaDataBaseDaLicitacao,
    
                                        sinapiPossuiOcorrenciaNaDataBaseDeReferencia: servicoApi.sinapiPossuiOcorrenciaNaDataBaseDeReferencia,
                                        custoUnitarioReferencia: servicoApi.vlCustoUnitarioRef,
                                        custoUnitarioAnalise: servicoApi.vlCustoUnitario,
                                        custoUnitarioDataBase: servicoApi.custoUnitarioDataBase,
                                        quantidade: servicoApi.quantidade,
                                        versao: servicoApi.versao
                                    };
                                })
                            };
                        })
                    };

                    return poMacrosservicoServicos;
                })
            );
    }

    loadEventosServicoPo(poId: number): Observable<EventoModel[]> {
        // const mock = new MacrosservicoMock();
        // return mock.listarEventosServico(macrosservicoId, servicoId);

        return this.http
            .get<any[]>(`${AppConfig.endpoint}/po/${poId}/eventos/`)
            .pipe(
                map((api) => {
                    const eventos: EventoModel[] = api.map(eventoApi => {
                        return {
                            id: eventoApi.id,
                            numeroEvento: eventoApi.numeroEvento,
                            tituloEvento: eventoApi.tituloEvento,
                            versao: eventoApi.versao
                        };
                    });

                    return eventos;
                })
            );
    }

    loadFrentesDeObraServicoPo(macrosservicoId: number, servicoId: number): Observable<FrenteDeObraQuantidadeModel[]> {
        // const mock = new MacrosservicoMock();
        // return mock.listarFrentesDeObraServico(macrosservicoId, servicoId);

        return this.http
            .get<any[]>(`${AppConfig.endpoint}/servico/${servicoId}/frente-obra`)
            .pipe(
                map((api) => {
                    const frentes: FrenteDeObraQuantidadeModel[] = api.map(frenteApi => {
                        return {
                            id: frenteApi.id,
                            quantidade: frenteApi.qtItens,
                            frenteDeObra: {
                                id: frenteApi.frenteObraFk,
                                nomeFrente: frenteApi.nomeFrente,
                                numeroFrente: frenteApi.numeroFrente,
                                versao: frenteApi.versao
                            }
                        };
                    });

                    return frentes;
                })
            );
    }

    loadServicoFrentesDeObraAnalise(servicoId: number): Observable<ServicoFrenteDeObraAnaliseModel[]> {
        return this.http
            .get<any[]>(`${AppConfig.endpoint}/servico/${servicoId}/frente-obra-analise`)
            .pipe(
                map((api) => {
                    const servicosFrenteObraAnalise: ServicoFrenteDeObraAnaliseModel[] = api.map(frenteApi => {
                        return {
                            idPO: frenteApi.idPO,
                            qtItens: frenteApi.qtItens,
                            servicoFk: frenteApi.servicoFk,
                            nomeFrente: frenteApi.nomeFrente,
                            numeroFrente: frenteApi.numeroFrente,
                            versao: frenteApi.versao
                        };
                    });

                    return servicosFrenteObraAnalise;
                })
            );
    }

    converterCffDTOVisaoParcelas (cffDTO: CFFSemEventosDTOModel): VisaoParcelasPorMacroServicoModel[] {

        const retorno: VisaoParcelasPorMacroServicoModel[] = [];

        if (cffDTO && cffDTO.listaMacroServicos) {

            for (const msDTO of cffDTO.listaMacroServicos) {

                const macroServico: VisaoParcelasPorMacroServicoModel = {
                    idMacroServico: msDTO.id,
                    nomeMacroServico: msDTO.txDescricao,
                    numeroMacroServico: msDTO.nrMacroServico,
                    precoTotal: msDTO.precoMacroservico,
                    parcelas: []
                };

                let totalAcumulado = 0;
                for (let i = 0; i < cffDTO.qtMesesDuracaoObra; i++) {

                    const numeroParcela = (i + 1);
                    const parcela: ParcelaCffSemEventosModel = {
                        idParcela: null,
                        numero: numeroParcela,
                        mesAnoParcela: this.recuperarMesAnoParcelaString(cffDTO.dtPrevisaoInicioObra, numeroParcela),
                        percentualParcela: 0,
                        percentualAcumulado: 0,
                        totalAcumulado: 0,
                        valorParcela: 0,
                        versao: 1
                    };

                    if (!msDTO.parcelas) {
                        continue;
                    }

                    const parcelaDTO = msDTO.parcelas.find(p => p.nrParcela === numeroParcela);

                    if (parcelaDTO) {
                        parcela.idParcela = parcelaDTO.id;
                        parcela.percentualParcela = parcelaDTO.pcParcela;
                        parcela.valorParcela = (msDTO.precoMacroservico * parcelaDTO.pcParcela);
                        parcela.versao = parcelaDTO.versao;
                    }

                    totalAcumulado += parcela.valorParcela;
                    parcela.totalAcumulado = totalAcumulado;
                    parcela.percentualAcumulado = (parcela.totalAcumulado / msDTO.precoMacroservico);

                    macroServico.parcelas.push(parcela);
                }

                retorno.push(macroServico);
            }
        }

        return retorno;
    }

    recuperarMesAnoParcelaString(dtPrevisaoInicioObra: string, nrParcela: number): string {
        let retorno = '';

        if (dtPrevisaoInicioObra && nrParcela) {
            const arrayData = (<any>dtPrevisaoInicioObra).split(/\D/);
            if (arrayData.length >= 3) {
                const data: Date = new Date(arrayData[0], arrayData[1] - 1, arrayData[2]);
                data.setMonth(data.getMonth() + nrParcela - 1);

                switch (data.getMonth()) {
                case 0:
                    retorno = 'Jan/';
                    break;
                case 1:
                    retorno = 'Fev/';
                    break;
                case 2:
                    retorno = 'Mar/';
                    break;
                case 3:
                    retorno = 'Abr/';
                    break;
                case 4:
                    retorno = 'Mai/';
                    break;
                case 5:
                    retorno = 'Jun/';
                    break;
                case 6:
                    retorno = 'Jul/';
                    break;
                case 7:
                    retorno = 'Ago/';
                    break;
                case 8:
                    retorno = 'Set/';
                    break;
                case 9:
                    retorno = 'Out/';
                    break;
                case 10:
                    retorno = 'Nov/';
                    break;
                case 11:
                    retorno = 'Dez/';
                    break;
                }
                retorno = retorno + data.getFullYear();
            }
        }
        return retorno;
      }

    saveServicoPo(servico: ServicoCadastroModel): any {
        const url = `${AppConfig.endpoint}/servico`;
        return this.http.post<{ status: string }>(url, servico);
    }

    recuperarCffComEventosDaListagem(idPo: number): Observable<CFFComEventosModel> {
        const cffDTOObservable: Observable<CFFComEventosModel> =
            this.http.get<CFFComEventosModel>(`${AppConfig.endpoint}/po/${idPo}/cffcomeventos/`);

        return cffDTOObservable;
    }

    saveCFFComEventos(idPO: number, idEvento: number, eventoModel: EventoCFFcomEventosModel): any {
        const url = `${AppConfig.endpoint}/po/${idPO}/cffcomeventos/${idEvento}`;
        return this.http.put<{ status: string }>(url, eventoModel);
    }

    atualizarReferenciaPo(idPo: number, referencia: string, versao: number): any {
        const url = `${AppConfig.endpoint}/po/${idPo}/?referencia=${referencia}&versao=${versao}`;
        return this.http.put<{ status: string }>(url, referencia);
    }

    recuperarPLQ(idPo: number): Observable<PLQModel> {
        const plqDTOObservable: Observable<PLQModel> =
            this.http.get<PLQModel>(`${AppConfig.endpoint}/po/${idPo}/plq/`);

        return plqDTOObservable;
    }

}
