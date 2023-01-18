import { Component, OnInit, OnDestroy } from '@angular/core';
import { SubmetaModel, MetaModel, SubmetaVRPLModel, QciStateModel } from '../../../../../model/qci/qci.state.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Store, Actions, ofActionSuccessful } from '@ngxs/store';
import { LicitacaoState } from '../../../../../model/licitacao/licitacao.state';
import { filter, take } from 'rxjs/operators';
import { Navigate } from '@ngxs/router-plugin';
import { CurrencyHelperService } from '../../../../../siconv/services/currency-helper.service';
import { SaveSubmetaVRPL, LoadQciLicitacao } from 'src/app/model/qci/qci.actions';
import { Subscription } from 'rxjs';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { AlertMessageService } from '@serpro/ngx-siconv';

@Component({
  selector: 'vrpl-cadastro-qci',
  templateUrl: './cadastro-qci.component.html'
})
export class CadastroQciComponent implements OnInit, OnDestroy {

  qci: QciStateModel;
  meta: MetaModel;
  submeta: SubmetaModel;
  qciForm: FormGroup;
  visualizar = false;
  submitted = false;
  qciSubscription: Subscription;
  actionsSubscription: Subscription;

  regexpNumber = /(\d{1,3}(\.\d{3})*|\d+)(\,\d+)$/;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private store: Store,
    private alertMessageService: AlertMessageService,
    private actions: Actions,
    private currencyHelper: CurrencyHelperService) { }

  ngOnInit() {

    const detalhar = 7;
    if (this.route.snapshot.pathFromRoot[detalhar].url[0].path === 'detalhar') {
      this.visualizar = true;
    }

    this.qciSubscription = this.store.select(LicitacaoDetalhadaState.qciLicitacaoDetalhada)
      .pipe(
        filter(qci => qci != null),
        take(1)
      ).subscribe((qci) => {
        const id = this.route.snapshot.paramMap.get('id');
        this.qci = qci;
        qci.metas.forEach(metaQci => {
          metaQci.submetas.forEach(submetaQci => {
            if (submetaQci.id === Number(id)) {
              this.submeta = submetaQci;
              this.meta = metaQci;
            }
          });
        });

        this.initForm();

      });

    this.actionsSubscription = this.actions
      .pipe(ofActionSuccessful(SaveSubmetaVRPL), take(1))
      .subscribe(() => {
        this.alertMessageService.success('Alteração da Submeta realizada com sucesso!');
        this.store.dispatch(new LoadQciLicitacao());
        this.store.dispatch(
          this.voltarListagemAction()
        );
      });
  }

  ngOnDestroy(): void {
    this.qciSubscription.unsubscribe();
    this.actionsSubscription.unsubscribe();
  }

  initForm() {
    const repasse = this.submeta.vlRepasse ? this.converterParaFormatadorSemArredondamento(this.submeta.vlRepasse) : 0;
    const outros = this.submeta.vlOutros ? this.converterParaFormatadorSemArredondamento(this.submeta.vlOutros) : 0;
    this.qciForm = this.fb.group({
      valorRepasse: [{ value: repasse, disabled: this.visualizar }, Validators.required ],
      valorOutrosLicitado: [{ value: outros, disabled: this.visualizar }, Validators.required ]
    });
  }

  converterParaFormatadorSemArredondamento(valor:number): number{
    
    var numerText = valor + '';
    var indicePonto = numerText.indexOf( "." );
    if (indicePonto == -1) {
      numerText = valor + '00';
    } else {
      var aposVirgula = numerText.substring(indicePonto + 1);
      var antesVirgula = numerText.substring(0,indicePonto);
      if (aposVirgula.length == 0) {
        antesVirgula = antesVirgula + '00'
      } else if (aposVirgula.length == 1) {
        antesVirgula = antesVirgula + aposVirgula + '0'
      } else if (aposVirgula.length == 2){
        antesVirgula = antesVirgula + aposVirgula;
      } else if (aposVirgula.length > 2){
        var digito = aposVirgula.substring(2,3);
        if (parseInt(digito) >= 5) {
          var arredondamento = parseInt(antesVirgula + aposVirgula.substring(0,2)) + 1;
          antesVirgula = arredondamento + '';
        } else {
          antesVirgula = antesVirgula + aposVirgula.substring(0,2);
        }
      }
      numerText = antesVirgula;
    }
    return parseInt(numerText);
  }

  validarValorRepasse(){

    if (!this.qciForm.get('valorRepasse').value.match(this.regexpNumber) ) {
      this.qciForm.get('valorRepasse').setValue(''); 
    }
  }

  validarValorOutros(){

    if (!this.qciForm.get('valorOutrosLicitado').value.match(this.regexpNumber)  ) {
      this.qciForm.get('valorOutrosLicitado').setValue(''); 
    }
  }

  onSubmit() {

    if (!this.qciForm.valid) {
      this.alertMessageService.error('Verifique campos obrigatórios!');
      return;
    }

    const idLicitacao = this.store.selectSnapshot(LicitacaoDetalhadaState).id;
    const repasse = this.currencyHelper.rawValue(this.qciForm.get('valorRepasse').value);
    const outros = this.currencyHelper.rawValue(this.qciForm.get('valorOutrosLicitado').value);

    if (repasse + outros > this.submeta.vlTotal) {
      this.alertMessageService.error('Repasse e Outros valores não podem ultrapassar Total Licitado!' );
      return;
    }

    const submeta: SubmetaVRPLModel = {
      idSubmeta: this.submeta.id,
      idMeta: this.meta.id,
      vlRepasse: repasse,
      vlOutros: outros,
      versao: this.submeta.versao
    };

    this.store.dispatch(new SaveSubmetaVRPL(submeta));
  }

  get contrapartida() {
    const repasse = this.currencyHelper.rawValue(this.qciForm.get('valorRepasse').value);
    const outros = this.currencyHelper.rawValue(this.qciForm.get('valorOutrosLicitado').value);
    return this.submeta.vlTotal - repasse - outros;
  }

  showError(fieldName: string) {
    const field = this.field(fieldName);
    return !field.valid && (!field.pristine || this.submitted);
  }

  field(fieldName: string) {
    return this.qciForm.get(fieldName);
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

  numberOnly(event): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;
  }

}
