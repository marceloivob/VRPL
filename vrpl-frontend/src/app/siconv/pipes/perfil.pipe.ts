import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'perfil'
})
export class PerfilPipe implements PipeTransform {

  transform(value: any, args?: any): any {
    if (typeof value !== 'string') {
      return value;
    }

    switch (value) {
      case 'PROPONENTE':
        return 'Proponente';
      case 'CONCEDENTE':
        return 'Concedente';
      case 'MANDATARIA':
        return 'Mandatária';
      default:
        return value;
    }

  }

}
