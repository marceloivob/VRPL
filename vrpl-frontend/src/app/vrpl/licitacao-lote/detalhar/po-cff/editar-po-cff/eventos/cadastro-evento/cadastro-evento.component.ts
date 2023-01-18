import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormBuilder, Validators, FormGroup } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Store, Actions, ofActionSuccessful } from '@ngxs/store';
import { BsLocaleService } from 'ngx-bootstrap';
import { Navigate } from '@ngxs/router-plugin';
import { EventoModel } from 'src/app/model/pocff/po-cff.state.model';
import { PoCffService } from 'src/app/model/pocff/po-cff.service';
import { Subscription, Observable, of } from 'rxjs';
import { SaveEvento } from 'src/app/model/pocff/po-cff.actions';
import { LicitacaoStateModel } from 'src/app/model/licitacao/licitacao.state.model';
import { map } from 'rxjs/operators';
import { AlertMessageService } from '@serpro/ngx-siconv';

@Component({
  selector: 'vrpl-cadastro-evento',
  templateUrl: './cadastro-evento.component.html'
})
export class CadastroEventoComponent implements OnInit, OnDestroy {
  visualizar: boolean;
  eventoForm: FormGroup;
  submitted: boolean;
  evento: EventoModel;
  actionSubscription$: Subscription;

  constructor(
    private fb: FormBuilder,
    private readonly route: ActivatedRoute,
    private readonly store: Store,
    private readonly poCff: PoCffService,
    private readonly actions: Actions,
    private alertMessageService: AlertMessageService) { }

  ngOnInit() {
    if (this.route.snapshot.url[0].path === 'detalhar') {
      this.visualizar = true;
    }

    this.initForm().subscribe(
      evento => {
        this.evento = evento;
        this.eventoForm = this.fb.group({
          id: [{ value: evento.id, disabled: this.visualizar }],
          numeroEvento: [{ value: evento.numeroEvento, disabled: this.visualizar }, [Validators.required, Validators.min(1), Validators.max(999)]],
          tituloEvento: [{ value: evento.tituloEvento, disabled: this.visualizar }, [Validators.required, Validators.maxLength(100)]],
          versao: evento.versao
        });
      }
    );
  }

  subscribeActions(mensagemSucesso: string) {
    this.actionSubscription$ = this.actions
      .pipe(ofActionSuccessful(SaveEvento))
      .subscribe(() => {
        this.alertMessageService.success(mensagemSucesso);
        this.store.dispatch(
          this.voltarListagemAction()
        );
      });
  }

  initForm(): Observable<EventoModel> {
    const idEvento = this.route.snapshot.paramMap.get('id');
    const idPO = this.getIdPO();

    if (idEvento) {
      this.subscribeActions('Alteração do Evento realizada com sucesso.');
      return this.poCff.loadEventoById(Number(idPO), Number(idEvento));
    } else {
      this.subscribeActions('Inclusão do Evento realizada com sucesso.');
      return this.poCff.recuperarSequencialEvento(idPO).pipe(
        map(sequencial => {
          return {
            id: null,
            numeroEvento: sequencial,
            tituloEvento: null,
            versao: 1
          };
        })
      );
    }
  }

  getIdPO() {
    const licitacaoState = this.store.selectSnapshot<LicitacaoStateModel>(
      stateSnapshot => stateSnapshot.licitacao
    );

    const po = licitacaoState.licitacaoDetalhada.po.poDetalhada;
    const idPO = po.id;

    return idPO;
  }

  ngOnDestroy() {
    this.actionSubscription$.unsubscribe();
  }

  onSubmit() {
    this.submitted = true;

    if (!this.eventoForm.valid) {
      this.alertMessageService.error('Verifique erros nos campos!');
      return;
    }

    const formValues = this.eventoForm.value;

    const evento: EventoModel = {
      id: formValues.id,
      numeroEvento: formValues.numeroEvento,
      tituloEvento: formValues.tituloEvento,
      versao: this.evento.versao
    };

    const idPO = this.getIdPO();

    this.store.dispatch(new SaveEvento(Number(idPO), evento));
  }

  showError(fieldName: string) {
    const field = this.field(fieldName);
    return !field.valid && (!field.pristine || this.submitted);
  }

  field(fieldName: string) {
    return this.eventoForm.get(fieldName);
  }

  numberOnly(event): boolean {
    const charCode = (event.which) ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
      return false;
    }
    return true;

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

}
