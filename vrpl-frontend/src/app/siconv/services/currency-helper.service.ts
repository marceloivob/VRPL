import { Injectable } from '@angular/core';

@Injectable()
export class CurrencyHelperService {
  DECIMAL_SEPARATOR = ',';
  THOUSANDS_SEPARATOR = '.';

  constructor() { }
  transform(value: number | string): string {
    value = value ? (+value).toString() : '0';

    if (value.length > 2) {
      const inteiro = value.substr(0, value.length - 2);
      const fracao = this.DECIMAL_SEPARATOR + value.substr(value.length - 2, value.length);
      return (inteiro + fracao).replace(/\B(?=(\d{3})+(?!\d))/g, this.THOUSANDS_SEPARATOR);
    } else if (value.length === 2) {
      const inteiro = 0;
      const fracao = this.DECIMAL_SEPARATOR + value;
      return (inteiro + fracao);
    } else if (value.length === 1) {
      const inteiro = 0;
      const fracao = this.DECIMAL_SEPARATOR + 0 + value.substr(value.length - 1, value.length);
      return (inteiro + fracao);
    }
    return value;
  }

  parse(value: string): string {
    const TAM_MAX = 22;
    if (value.length > TAM_MAX) {
      value = value.substr(0, TAM_MAX);
    }

    value = value.replace(/\./gi, '');
    value = value.replace(/\,/gi, '');
    value = value ? (+value).toString() : '';


    return value;
  }

  rawValue(value: number): number {
    if (!value) {
      return 0;
    }

    let valor_trasformado = this.parse(value.toString());

    valor_trasformado = this.transform(valor_trasformado);
    valor_trasformado = valor_trasformado.replace(/\./gi, '');
    valor_trasformado = valor_trasformado.replace(/\,/gi, '.');

    return Number(valor_trasformado);
  }
}
