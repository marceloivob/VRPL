import { PoDetalhadaModel, PoModel } from './../../../../../model/pocff/po-cff.state.model';
import { Observable } from 'rxjs';
import { LoadPoDetalhada } from './../../../../../model/pocff/po-cff.actions';
import { ActivatedRoute } from '@angular/router';
import { filter, take } from 'rxjs/operators';
import { Store, Select } from '@ngxs/store';
import { Component, OnInit } from '@angular/core';
import { LicitacaoDetalhadaState } from 'src/app/model/licitacao/licitacao.detalhada.state';
import { PoLicitacaoState } from 'src/app/model/licitacao/pocff/po-licitacao.state';
import { LicitacaoDetalhadaModel } from 'src/app/model/licitacao/licitacao.state.model';

@Component({
  selector: 'vrpl-editar-po-cff',
  templateUrl: './editar-po-cff.component.html',
  styleUrls: ['./editar-po-cff.component.scss']
})

export class EditarPoCffComponent implements OnInit {

  @Select(PoLicitacaoState.poDetalhada) poDetalhada: Observable<PoDetalhadaModel>;
  acompanhamentoEventos = false;

  licitacaoDetalhada: LicitacaoDetalhadaModel;
  po: PoModel;

  constructor(
    private readonly store: Store,
    private readonly route: ActivatedRoute
  ) { }

  ngOnInit() {
      this.loadPoDetalhada();
  }

  loadPoDetalhada() {

    this.store.select(LicitacaoDetalhadaState)
      .pipe(
        filter( licitacaoDetalhada => licitacaoDetalhada.po != null),
        filter( licitacaoDetalhada => licitacaoDetalhada.po.poCff != null),
        take(1)
      ).subscribe( (licitacaoDetalhada) => {
        this.licitacaoDetalhada = licitacaoDetalhada;
        const id = this.route.parent.snapshot.paramMap.get('id');
        const apenasVisualizar = this.route.parent.snapshot.pathFromRoot[7].url[0].path === 'detalhar';
        this.store.dispatch(new LoadPoDetalhada(Number(id), apenasVisualizar))
          .subscribe(() => {
            this.poDetalhada.pipe(
              filter( poDetalhada => poDetalhada != null),
              take(1)
            ).subscribe( poDetalhada => {
              this.acompanhamentoEventos = poDetalhada.acompanhamentoEvento;
              this.licitacaoDetalhada.po.poCff.forEach( poSelecionada => {
                if (poSelecionada.id === poDetalhada.id) {
                  this.po = poSelecionada;
                }
              });
            });
          });
      });
  }

}
