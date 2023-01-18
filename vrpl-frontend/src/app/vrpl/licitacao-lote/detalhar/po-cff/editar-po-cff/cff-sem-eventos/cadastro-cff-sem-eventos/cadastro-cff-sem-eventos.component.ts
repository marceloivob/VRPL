import { Component, OnInit, OnDestroy } from '@angular/core';
import { Observable, Subscription, of } from 'rxjs';
import { VisaoParcelasPorMacroServicoModel, ParcelaCffSemEventosModel, PoDetalhadaModel,
  MacroServicoParcelaDTOModel,
  MacroServicoCFFDTOModel} from '../../../../../../../model/pocff/po-cff.state.model';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Store, Actions, ofActionSuccessful } from '@ngxs/store';
import { LicitacaoStateModel } from '../../../../../../../model/licitacao/licitacao.state.model';
import { Navigate } from '@ngxs/router-plugin';
import { SaveCFFSemEventos } from '../../../../../../../model/pocff/po-cff.actions';
import { AlertMessageService } from '@serpro/ngx-siconv';

@Component({
  selector: 'vrpl-cadastro-cff-sem-eventos',
  templateUrl: './cadastro-cff-sem-eventos.component.html'
})

export class CadastroCffSemEventosComponent implements OnInit, OnDestroy {

  visualizar: boolean;
  visaoParcelasForm: FormGroup;
  visaoParcelas: VisaoParcelasPorMacroServicoModel;
  parcelasInvalidas: boolean;
  actionSubscription$: Subscription;
  po: PoDetalhadaModel;

  constructor(
    private fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly store: Store,
    private alertMessageService: AlertMessageService,
    private readonly actions: Actions
  ) { }

  ngOnInit() {

    this.visualizar = (this.route.snapshot.url[0].path === 'detalhar');

    this.parcelasInvalidas = false;

    this.subscribeActions();

    this.initForm().subscribe(
      visao => {

        const parcelasVisao: ParcelaCffSemEventosModel[] = [];

        // é preciso criar uma copia na mao, nem {...visao} funciona
        for (const p of visao.parcelas) {
          parcelasVisao.push({
            idParcela: p.idParcela,
            numero: p.numero,
            mesAnoParcela: p.mesAnoParcela,
            percentualParcela: p.percentualParcela,
            percentualAcumulado: p.percentualAcumulado,
            totalAcumulado: p.totalAcumulado,
            valorParcela: p.valorParcela,
            versao: p.versao
          });
        }

        this.visaoParcelas = {
          idMacroServico: visao.idMacroServico,
          numeroMacroServico: visao.numeroMacroServico,
          nomeMacroServico: visao.nomeMacroServico,
          precoTotal: visao.precoTotal,
          parcelas: parcelasVisao
        };

        this.visaoParcelasForm = this.fb.group({
          numeroMacroServico:  [{ value: this.visaoParcelas.numeroMacroServico, disabled: true }],
          nomeMacroServico: [{ value: this.visaoParcelas.nomeMacroServico, disabled: true }],
           valorTotal: [{ value: this.visaoParcelas.precoTotal, disabled: true }],
           parcelas: [{ value: this.visaoParcelas.parcelas }]
        });
      }
    );
    // para atualizar o parcelasInvalidas
    this.recalcularParcelas();
  }

  subscribeActions() {
    this.actionSubscription$ = this.actions
      .pipe(ofActionSuccessful(SaveCFFSemEventos))
      .subscribe(() => {
        this.alertMessageService.success('Cronograma Físico Financeiro salvo com sucesso!');
        this.store.dispatch(
          this.voltarListagemAction()
        );
      });
  }

  initForm(): Observable<VisaoParcelasPorMacroServicoModel> {
    const licitacaoState = this.store.selectSnapshot<LicitacaoStateModel>(
      stateSnapshot => stateSnapshot.licitacao
    );

    this.po = licitacaoState.licitacaoDetalhada.po.poDetalhada;

    const idVisao = this.route.snapshot.paramMap.get('id');

    if (idVisao) {
      return of(this.po.listaParcelasPorMacroServico.find(
        visao => visao.idMacroServico === parseInt(idVisao, 10)));
    }
  }

  voltar() {
    this.store.dispatch(this.voltarListagemAction());
  }

  voltarListagemAction() {
    const id = this.route.snapshot.paramMap.get('id');
    let caminho;
    if (id) {
      caminho = '../../listagem';
    } else {
      caminho = '../listagem';
    }
    return new Navigate([caminho], {}, { relativeTo: this.route });
  }

  onSubmit() {
    if (!this.visaoParcelasForm.valid) {
      this.alertMessageService.error('Verifique erros nos campos!');
      return;
    }

    if (this.parcelasInvalidas) {
      this.alertMessageService
        .error('O valor percentual acumulado excede o limite de 100%. Favor verificar a distribuição dos percentuais nas parcelas.');
      return;
    }

    const parcelas: MacroServicoParcelaDTOModel[] = [];

    for (const p of this.visaoParcelas.parcelas) {
      parcelas.push({
        id: p.idParcela,
        macroServicoFk: this.visaoParcelas.idMacroServico,
        nrParcela: p.numero,
        pcParcela: p.percentualParcela,
        versao: p.versao
      });
    }

    const macroServico: MacroServicoCFFDTOModel = {
      id: this.visaoParcelas.idMacroServico,
      idPO: this.po.id,
      txDescricao: this.visaoParcelas.nomeMacroServico,
      nrMacroServico: this.visaoParcelas.numeroMacroServico,
      precoMacroservico: this.visaoParcelas.precoTotal,
      parcelas: parcelas
    };

    this.store.dispatch(new SaveCFFSemEventos(Number(this.po.id), macroServico));
  }

  onKey(event: any) {
    this.recalcularParcelas();
  }

  ngOnDestroy() {
    this.actionSubscription$.unsubscribe();
  }

  recalcularParcelas() {
    let totalAcumulado = 0;
    let totalPercentual = 0;

    for (const parcela of this.visaoParcelas.parcelas) {
      parcela.percentualParcela = parseFloat(parcela.percentualParcela.toFixed(2));
      totalPercentual += parcela.percentualParcela;
      parcela.valorParcela = parseFloat((this.visaoParcelas.precoTotal * parcela.percentualParcela).toFixed(2));
      totalAcumulado += parcela.valorParcela;
      parcela.totalAcumulado = totalAcumulado;
      parcela.percentualAcumulado = parseFloat((parcela.totalAcumulado / this.visaoParcelas.precoTotal).toFixed(2));
    }

    // this.parcelasInvalidas = (totalAcumulado / 100) > this.visaoParcelas.precoTotal;
    this.parcelasInvalidas = totalPercentual > 100;
  }

}
