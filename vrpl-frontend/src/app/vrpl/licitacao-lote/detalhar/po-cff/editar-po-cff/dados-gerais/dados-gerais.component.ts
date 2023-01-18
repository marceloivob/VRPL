import { BsLocaleService, BsModalRef, BsModalService } from 'ngx-bootstrap';
import { filter, take, map } from 'rxjs/operators';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { LicitacaoDetalhadaModel } from './../../../../../../model/licitacao/licitacao.state.model';
import { Subscription, Observable } from 'rxjs';
import { SavePoDadosGerais, LoadPoDetalhada, LoadPoCffLicitacao, ValidarDataBasePo } from './../../../../../../model/pocff/po-cff.actions';
import { PoModel, PoDetalhadaModel } from './../../../../../../model/pocff/po-cff.state.model';
import { Navigate } from '@ngxs/router-plugin';
import { ActivatedRoute } from '@angular/router';
import { Store, ofActionSuccessful, Actions, Select } from '@ngxs/store';
import { Component, OnInit, OnDestroy, TemplateRef } from '@angular/core';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { PoLicitacaoState } from 'src/app/model/licitacao/pocff/po-licitacao.state';
import { AlertMessageService } from '@serpro/ngx-siconv';

@Component({
  selector: 'vrpl-dados-gerais',
  templateUrl: './dados-gerais.component.html'
})
export class DadosGeraisComponent implements OnInit, OnDestroy {

  modalRef: BsModalRef;
  poForm: FormGroup;
  submitted = false;
  idPo: number;

  previsaoInicioVRPL: Date;
  inicioObraAnalise: Date;
  qtMesesDuracaoObra: number;
  visualizar = false;
  proponente = false;

  licitacaoDetalhada: LicitacaoDetalhadaModel;
  po: PoModel;
  inTrabalhoSocial = false;

  @Select(PoLicitacaoState.poDetalhada) poDetalhada: Observable<PoDetalhadaModel>;

  licitacaoDetalhadaSubscription: Subscription;
  saveActionDadosGerais: Subscription;
  poDetalhadaSubscription: Subscription;

  constructor(
    private readonly store: Store,
    private readonly route: ActivatedRoute,
    private readonly alertMessageService: AlertMessageService,
    private readonly actions: Actions,
    private readonly fb: FormBuilder,
    private readonly localeService: BsLocaleService,
    private readonly modalService: BsModalService

  ) { }

  ngOnInit() {
    this.actions.pipe(
      ofActionSuccessful(LoadPoDetalhada),
      take(1)
    ).subscribe(() => this.initComponents());
  }

  initComponents() {
    this.localeService.use('pt-br');
    // carregar a PO
    this.licitacaoDetalhadaSubscription = this.store.select(LicitacaoDetalhadaState)
      .pipe(
        filter( licitacaoDetalhada => licitacaoDetalhada != null),
        filter( licitacaoDetalhada => licitacaoDetalhada.po != null),
        filter( licitacaoDetalhada => licitacaoDetalhada.po.poCff != null),
        map( licitacaoDetalhada =>  <LicitacaoDetalhadaModel> licitacaoDetalhada),
        take(1)
      ).subscribe( (licitacaoDetalhada) => {
        const id = this.route.parent.snapshot.paramMap.get('id');
        this.idPo = Number(id);

        this.licitacaoDetalhada = licitacaoDetalhada;

        licitacaoDetalhada.po.poCff.forEach( poSelecionada => {
            if (poSelecionada.id === Number(id)) {
              this.po = poSelecionada;
            }
          });
      });

      this.saveActionDadosGerais =  this.actions
      .pipe(ofActionSuccessful(SavePoDadosGerais), take(1))
      .subscribe(() => {
        this.alertMessageService.success('Alteração dos dados da PO realizada com sucesso!');
        this.store.dispatch(new LoadPoCffLicitacao(this.licitacaoDetalhada.id));
        this.store.dispatch(new LoadPoDetalhada(this.idPo));
        this.store.dispatch(
          this.voltarListagemAction()
        );
      });

      this.poDetalhadaSubscription = this.poDetalhada.pipe(
        filter( poDetalhada => poDetalhada != null),
        take(1)
      ).subscribe( poDetalhada => {
        this.visualizar = poDetalhada.apenasVisualizar || false;
        if (this.route.parent.parent.snapshot.url[0].path === 'detalhar') {
          this.visualizar = true;
        }
        this.inTrabalhoSocial = poDetalhada.inTrabalhoSocial;
        this.initForm();
      });
  }

  ngOnDestroy(): void {
    this.licitacaoDetalhadaSubscription.unsubscribe();
    this.saveActionDadosGerais.unsubscribe();
    this.poDetalhadaSubscription.unsubscribe();
  }

  initForm() {
    const arrayData = (<any>this.po.previsaoInicioVRPL).split(/\D/);
    const arrayDataBase = (<any>this.po.dataBaseVrpl).split(/\D/);
    this.poForm = this.fb.group({
      previsaoInicioObra: [ { value: new Date(arrayData[0], arrayData[1] - 1, arrayData[2]),
        disabled: this.visualizar }, [Validators.required] ],
      duracaoObra: [ { value: this.po.duracao, disabled: this.visualizar },
        [Validators.required, Validators.min(1), Validators.max(999), Validators.maxLength(3)]],
      dataBase: [ { value: new Date(arrayDataBase[0], arrayDataBase[1] - 1, arrayDataBase[2]),
          disabled: this.visualizar }, [Validators.required] ]
    });

  }

  isInicioObraValido() {
    const inicioObra = this.poForm.get('previsaoInicioObra').value;
    const arrayData = (<any>this.po.previsaoAnalise).split(/\D/);

    this.inicioObraAnalise = new Date(arrayData[0], arrayData[1] - 1, arrayData[2]);

    inicioObra.setHours(0, 0, 0, 0); // Remove a hora e o TimeZone
    this.inicioObraAnalise.setHours(0, 0, 0, 0); // Remove a hora e o TimeZone

    return inicioObra && inicioObra >= this.inicioObraAnalise;
  }

  submit(reaproveitaCFFSemEventos: boolean) {

    this.submitted = true;

    const inicioObra = this.poForm.get('previsaoInicioObra').value;

    inicioObra.setDate(1); // Padroniza o dia do mês para não influenciar na validação abaixo
    inicioObra.setHours(0, 0, 0, 0); // Remove a hora e o TimeZone

    if (!this.poForm.valid || !this.isInicioObraValido() || this.isDataBaseInvalida()) {
      this.alertMessageService.error('Verifique os erros nos campos!');
      return;
    }

    // Recuperar os campos que foram preenchidos
    const duracaoObra = this.poForm.get('duracaoObra').value;
    const dataBaseVrpl = this.poForm.get('dataBase').value;

    const po:  PoDetalhadaModel = {
      id: this.idPo,
      idAnalise: this.po.idAnalise,
      dataBaseAnalise: this.po.dataBaseAnalise,
      dataBaseVrpl: dataBaseVrpl,
      localidade: this.po.localidade,
      previsaoAnalise: this.po.previsaoAnalise,
      previsaoInicioVRPL: inicioObra,
      duracao: this.po.duracao,
      qtMesesDuracaoObra: duracaoObra,
      qtMesesDuracaoObraValorOriginal: this.po.qtMesesDuracaoObraValorOriginal,
      acompanhamentoEvento: this.po.acompanhamentoEvento,
      precoTotalAnalise: this.po.precoTotalAnalise,
      descricao: this.po.descricao,
      descricaoMeta : this.po.descricaoMeta,
      numeroMeta : this.po.numeroMeta,
      numeroSubmeta: this.po.numeroSubmeta,
      inTrabalhoSocial: this.po.inTrabalhoSocial,
      reaproveitaCFFSemEventos: reaproveitaCFFSemEventos,
      versao: this.po.versao
    };

    this.store.dispatch(new SavePoDadosGerais(po));
    this.store.dispatch(new ValidarDataBasePo(po));
  }

  confirmarAcao() {
    this.submit(true);
    this.modalRef.hide();
  }

  cancelarAcao() {
    this.submit(false);
    this.modalRef.hide();
  }

  confirmaAlteracao (template: TemplateRef<any>) {
    const duracaoObra = this.poForm.get('duracaoObra').value;

    // Defeito 1818934
    // Se a "Duração da Obra" for IGUAL a "Duração definida da Obra":
    if (duracaoObra === this.po.qtMesesDuracaoObraValorOriginal) {
      // Sistema exibe mensagem "Deseja reaproveitar o CFF sem eventos?"
      this.modalRef = this.modalService.show(template);
    } else {
      // Caso a "Duração da Obra" for DIFERENTE a "Duração definida da Obra" todas as abas de CFF (com e sem eventos)
      // virão em branco para preenchimento .
      this.submit(null);
    }
  }

  get podeEditar() {
    return this.proponente;
  }

  showError(fieldName: string) {
    const field = this.field(fieldName);
    return field && !field.valid && (!field.pristine || this.submitted);
  }

  showErrorDataInicioObra() {
    const field = this.field('previsaoInicioObra');
    return field && field.value &&
      !this.isInicioObraValido() &&
      (!field.pristine || this.submitted);
  }

  showErrorDataBaseMaiorQueAtual() {
    const field = this.field('dataBase');
    return field && field.value &&
      this.isDataBaseMaiorQueAtual() &&
      (!field.pristine || this.submitted);
  }

  isDataBaseMaiorQueAtual() {

    const dataBase = this.poForm.get('dataBase').value;

    const hoje: Date = new Date();
    hoje.setHours(0, 0, 0, 0); // Remove a hora e o TimeZone

    return !dataBase || dataBase > hoje;
  }

  showErrorDataBaseMenorQueAnalise() {
    const field = this.field('dataBase');
    return field && field.value &&
      this.isDataBaseMenorQueAnalise() &&
      (!field.pristine || this.submitted);
  }

  isDataBaseMenorQueAnalise() {
    const dataBase = this.poForm.get('dataBase').value;

    const arrayDataAnalise = (<any>this.po.dataBaseAnalise).split(/\D/);
    const dataBaseAnalise = new Date(arrayDataAnalise[0], arrayDataAnalise[1] - 1, arrayDataAnalise[2]);

    return !dataBase || dataBase < dataBaseAnalise;
  }

  isDataBaseInvalida() {
    return this.isDataBaseMaiorQueAtual() || this.isDataBaseMenorQueAnalise();
  }

  field(fieldName: string) {
    return this.poForm.get(fieldName);
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
      caminho = '../../../listagem';
    }

    return new Navigate([caminho], {}, { relativeTo: this.route });
  }

  numberOnly(event): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;

  }

}
