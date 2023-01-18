import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
    name: 'ig'
})
// Inscricao Genérica
export class InscricaoGenericaPipe implements PipeTransform {
    transform(value: any): any {
        return value;
    }
}
