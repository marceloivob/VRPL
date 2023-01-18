import { Component, ViewChildren, Output, EventEmitter, Input, TemplateRef } from '@angular/core';
import { BaseComponent } from 'src/app/siconv/util/base.component';
import { Store } from '@ngxs/store';
import { LoteInputComponent } from './lote-input/lote-input.component';
import { LoteModel, LicitacaoModel } from 'src/app/model/licitacao/licitacao.state.model';
import { BsModalRef, BsModalService } from 'ngx-bootstrap';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';

interface SubmetasList {
  idSubmeta: number;
  nomeMeta: string;
  nomeSubmeta: string;
  numero: number;
  versaoSubmeta: number;
  social: boolean;
  numeroLote: number;
  idLote: number;
  evento: boolean;
  regimeExecucao: string;
  idLicitacao: number;
}


@Component({
  selector: 'vrpl-edicao-submetas',
  templateUrl: './edicao-submetas.component.html',
  styleUrls: []
})
export class EdicaoSubmetasComponent extends BaseComponent {

  @ViewChildren(LoteInputComponent) loteInputs: LoteInputComponent[];
  submetas: SubmetasList[];
  lotesExcluidos: number[];
  modalRef: BsModalRef;
  statusLicitacoes: Map<Number, String>;

  @Input() lotes: LoteModel[];
  @Input() licitacoes: LicitacaoModel[];
  @Output() salvar = new EventEmitter<any>();
  @Output() cancelar = new EventEmitter<any>();

  constructor(
    protected readonly  store: Store,
    private readonly modalService: BsModalService,
  ) {
    super(store);
  }

  init() {
    this.submetas = this.lotes
        .map( lote => {
          return lote.submetas.map( submeta => {
            return {
              idSubmeta: submeta.id,
              versaoSubmeta: submeta.versao,
              nomeMeta: submeta.meta,
              nomeSubmeta: submeta.submeta,
              numero: submeta.numero,
              social: submeta.social,
              numeroLote: lote.numero,
              idLote: lote.id,
              idLicitacao: lote.idLicitacao,
              evento: submeta.porEventos,
              regimeExecucao: submeta.regimeExecucao
            };
          });
        })
        .reduce((a, b) => a.concat(b), [])
        .sort( (a, b) => {
          let comp = a.numeroLote - b.numeroLote;
          if (comp === 0) {
            comp = a.numero - b.numero;
          }

          return comp;

        } );
    this.statusLicitacoes = new Map<Number, String>();
    for (let i = 0; i < this.licitacoes.length; i++) {
      this.statusLicitacoes.set(this.licitacoes[i].id, this.licitacoes[i].situacaoDaLicitacao);
    }
  }

  existemLotesExcluidos() {
    let index = 0;
    this.lotesExcluidos = [];
    const numerosLotes = this.lotes.filter( lote => lote.submetas.length > 0 );
    for (; index < numerosLotes.length; index++) {
      const numeroLote = numerosLotes[index].numero;
      const aindaExiste = this.loteInputs.some(
        lote => {
          const numeroLoteEditado = lote.loteAlterado ? lote.loteAlterado.novoLote : lote.numeroLote;
          return numeroLoteEditado === numeroLote;
        }
      );
      if ( !aindaExiste ) {
        this.lotesExcluidos = [...this.lotesExcluidos, numeroLote];
      }
    }

    return this.lotesExcluidos.length > 0;
  }

  exibeModalConfirmacao(template: TemplateRef<any>) {
    if (this.existemLotesExcluidos()) {
      this.modalRef = this.modalService.show(template);
    } else {
      this.confirmaSalvar();
    }
  }

  onVoltar() {
    this.cancelar.emit();
  }

  cancelarSalvar() {
    this.modalRef.hide();
    this.modalRef = null;
  }

  confirmaSalvar() {
    if (this.modalRef) {
      this.modalRef.hide();
      this.modalRef = null;
    }
    this.salvar.emit(
      this.loteInputs
        .map( input => input.loteAlterado )
        .filter( loteAlterado => loteAlterado )
        .map( loteAlterado => {
          const versaoSubmeta = this.submetas
            .find( submeta => submeta.idSubmeta === loteAlterado.idSubmeta )
            .versaoSubmeta;
          return {...loteAlterado, versaoSubmeta};
        })
    );
  }

}
