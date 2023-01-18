import { Component, ViewChild, TemplateRef } from '@angular/core';
import { Select, Store, Actions, ofActionSuccessful } from '@ngxs/store';
import { PropostaState } from '../model/proposta/proposta.state';
import { Observable } from 'rxjs';
import { PropostaStateModel } from '../model/proposta/proposta.state.model';
import { User, AlertMessageService } from '@serpro/ngx-siconv';
import { SiconvLegacyService } from '../model/siconv/siconv-legacy.service';
import { UserAuthorizerService } from '../model/user/user-authorizer.service';
import { BaseComponent } from '../siconv/util/base.component';
import { AppConfig } from '../core/app.config';
import { LoadProposta, VerificaFornecedorObsoleto } from '../model/proposta/proposta.actions';
import { RefreshToken} from '../model/user/user.actions';
import { LoadLicitacoesAtivasProposta, LoadLicitacoesRejeitadasProposta } from '../model/licitacao/licitacao.actions';
import { Navigate } from '@ngxs/router-plugin';
import { ActivatedRoute} from '@angular/router';
import { SaveNovoEstadoDocumentacao } from '../model/quadroresumo/quadroresumo.actions';
import { BsModalService, BsModalRef, ModalOptions } from 'ngx-bootstrap';
import { MenuComponent } from '../siconv/menu/menu.component';

@Component({
  selector: 'vrpl-main',
  templateUrl: './vrpl.component.html'
})
export class VrplComponent extends BaseComponent {

  @ViewChild('templateSessao') elementTemplate: TemplateRef<any>;
  @Select(PropostaState) propostaObservable: Observable<PropostaStateModel>;

  @ViewChild(MenuComponent)
  private menuComponent: MenuComponent;

  menuLoaded = false;
  menu: any;
  usuario: User;
  exibeMenu = true;

  tempoTotalSessao = 30;
  warningTime = 5;

  versoes: number[];
  versaoSelecionada: number;

  ehPrimeiraVezQueEntra = true;

  modalRef: BsModalRef;

  constructor(
    protected store: Store,
    private readonly route: ActivatedRoute,
    private service: SiconvLegacyService,
    private authorizer: UserAuthorizerService,
    private alertMessageService: AlertMessageService,
    private readonly modalService: BsModalService,

    private readonly actions$: Actions) {
    super(store);

    const versaoInicialPadrao = 0;

    this.versaoSelecionada = versaoInicialPadrao;
    this.ehPrimeiraVezQueEntra = true;
  }

  init() {
    this.getUsuario();
    this.recuperarMenu();
  }

  getUsuario() {
    this.usuario = new User(this.authorizer.user.name);
  }

  getPasswordUrl() {
    return AppConfig.urlToSICONVService + '/siconv/secure/TrocaDeSenhaObrigatoriaProcessar.do?menu=true';
  }

  getProfileUrl() {
    return AppConfig.urlToSICONVService + '/siconv/participe/AtualizarDadosUsuario/AtualizarDadosUsuario.do';
  }

  sessionTimeFeedback(sessionTime) {
    if (sessionTime === 'expired') {
      this.modalRef.hide();
      this.alertMessageService.warn('Sessão Expirada');

      window.location.href = AppConfig.urlToSICONVService + '?LLO=true';
    }

    if (sessionTime === 'warning') {
      this.alertMessageService.warn('Sessão expirará');
      this.modalRef = this.modalService.show(this.elementTemplate);
    }
  }

  logoutFeedback(logout) {
    window.location.href = AppConfig.urlToSICONVService + '?LLO=true';
  }

  recuperarMenu() {
    this.service.getMenu().subscribe(
      (values: any) => {
        this.menuLoaded = true;
        this.menu = values;
      }
    );
  }

  loadActions() {
    return [
      new LoadProposta(this.versaoSelecionada),
      new LoadLicitacoesAtivasProposta(this.versaoSelecionada),
      new LoadLicitacoesRejeitadasProposta(this.versaoSelecionada)
    ];
  }

  onLoad() {
    this.store.select(PropostaState)
      .pipe(this.takeUntilDestroyed())
      .subscribe(proposta => {

        if (this.ehPrimeiraVezQueEntra) {

          this.versaoSelecionada = this.getUltimaVersaoDaProposta(proposta);
          this.versoes = proposta.versoes;
          this.ehPrimeiraVezQueEntra = false;

          this.reloadProposta();

        } else {
          this.ehPrimeiraVezQueEntra = false;
          this.versaoSelecionada = proposta.versaoSelecionada;
          this.versoes = proposta.versoes;
        }

        this.store.dispatch(new Navigate(['./vrpl/licitacao-lote']));
      });
  }

  private getUltimaVersaoDaProposta(proposta) {
    // As versões estão ordenadas de forma descendente.
    // Então a última versão é o primeiro elemento do array
    return proposta.versoes[0];
  }

  versaoSelecionadaChanged(versaoSelecionada: any) {
    if (versaoSelecionada === 'selecione') {
      return;
    }

    this.ehPrimeiraVezQueEntra = false;

    this.reloadProposta();

  }

  private reloadProposta() {
    this.store.dispatch([
      new LoadProposta(Number(this.versaoSelecionada)),
      new LoadLicitacoesAtivasProposta(this.versaoSelecionada),
      new LoadLicitacoesRejeitadasProposta(this.versaoSelecionada)
    ]);
  }

  confirmaReinicio() {
    this.modalRef.hide();
    this.store.dispatch(new RefreshToken());
    this.menuComponent.minutos = this.tempoTotalSessao - 1;
    this.menuComponent.segundos = 60;
    this.exibeMenu = false;
    setTimeout(() => this.exibeMenu = true, 0);
  }

  cancelaReinicio() {
    this.modalRef.hide();
  }
}
/* This is the Modal Component */

