import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'simnao'
})
export class SimNaoPipe implements PipeTransform {

  transform(value: any): any {
    if (typeof value !== 'boolean') {
        return value;
    }

    if (value) {
      return 'Sim';
    }

    return 'Não';
  }

}
