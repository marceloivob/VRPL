import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { AlertMessageService } from '@serpro/ngx-siconv';

@Component({
  selector: 'vrpl-lote-input',
  templateUrl: './lote-input.component.html',
  styles: []
})
export class LoteInputComponent implements OnInit {

  @Input() numeroLote: number;
  @Input() idSubmeta: number;

  @Input() submetas: any[];

  @Input() statusLicitacoes: Map<Number, String>;

  novoLote: number;
  antigoLote: number;
  constructor(private alertMessageService: AlertMessageService) { }

  ngOnInit() {
    this.novoLote = this.numeroLote;
    this.antigoLote = this.numeroLote;
  }

  public get loteAlterado() {
    this.novoLote = Number(this.novoLote);
    if (this.novoLote === this.antigoLote) {
      return null;
    }

    return {
      novoLote: this.novoLote,
      antigoLote: this.antigoLote,
      idSubmeta: this.idSubmeta
    };
  }

  onChange() {
    if (!this.podeAlterar()) {
      this.novoLote = this.antigoLote;
    }
  }

  private podeAlterar(): boolean {
    this.novoLote = Number(this.novoLote);
    if (this.novoLote == undefined || this.novoLote == NaN || this.novoLote < 0) {
      this.alertMessageService.error('É preciso preencher um número de lote válido!');
      return false;
    }

    const submetaLote = this.submetas.find( submeta => submeta.idSubmeta === this.idSubmeta );
    let atividadeInvalida = false;
    let eventoInvalido = false;
    let execucaoInvalida = false;
    let licitacaoInvalida = false;
    let statusLicitacaoOrigemInvalida = false;
    let statusLicitacaoDestinoInvalida = false;
    let lotefound = false;
    let submetaNaoEditavel = false;
    let i = 0;
    for (; i < this.submetas.length; i++) {
      const submeta = this.submetas[i];
      if (submeta.numeroLote === this.novoLote) {
        lotefound = true;
        atividadeInvalida = this.submetas[i].social !== submetaLote.social;
        eventoInvalido = this.submetas[i].evento !== submetaLote.evento;
        execucaoInvalida = this.submetas[i].regimeExecucao !== submetaLote.regimeExecucao;

        licitacaoInvalida = submetaLote.idLicitacao && submetaLote.idLicitacao !== this.submetas[i].idLicitacao;
        const statusDestino = this.statusLicitacoes.get(this.submetas[i].idLicitacao);

        // so pode sair de um lote nao associado para na mesma situacao
        statusLicitacaoOrigemInvalida = submetaLote.idLicitacao !== null;
        statusLicitacaoDestinoInvalida = this.submetas[i].idLicitacao != null;

        submetaNaoEditavel = this.statusLicitacoes.has(submetaLote.idLicitacao);

        if (atividadeInvalida || eventoInvalido || execucaoInvalida || licitacaoInvalida
            || statusLicitacaoOrigemInvalida || statusLicitacaoDestinoInvalida || 
            submetaNaoEditavel) {
          break;
        }
      }
    }

    if (!lotefound) {
      submetaNaoEditavel = this.statusLicitacoes.has(submetaLote.idLicitacao);
    }



    if (licitacaoInvalida) {
      this.alertMessageService.error('Não é possível informar lote associado a outra licitação!');
      return false;
    }

    if (atividadeInvalida) {
      this.alertMessageService.error('Não é possível associar Trabalho Social com Engenharia em um mesmo lote!');
      return false;
    }

    if (eventoInvalido) {
      this.alertMessageService.error(
        'Não é possível associar submetas de Planilhas ' +
        'Orçamentárias acompanhadas por evento e sem acompanhamento por evento em um mesmo lote!'
      );
      return false;
    }

    if (execucaoInvalida) {
      this.alertMessageService.error('Não é possível associar submetas com regimes de execução diferentes!');
      return false;
    }


    if (statusLicitacaoOrigemInvalida) {
      this.alertMessageService.error('O Lote pertence a uma licitação que não pode ser alterada!');
      return false;
    }

    if (statusLicitacaoDestinoInvalida) {
      this.alertMessageService.error('Não é possivel associar a este Lote pois pertence a uma Licitação que não permite alteração!');
      return false;
    }

    if (submetaNaoEditavel) {
      this.alertMessageService.error('Não é possivel editar submeta já associada');
      return false;
    }

    submetaLote.numeroLote = this.novoLote;
    return true;
  }

}
