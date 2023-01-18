import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AppConfig } from 'src/app/core/app.config';
import { UfModel } from './uf.state.model';

@Injectable({
    providedIn: 'root'
})

export class UFService {

    constructor(private readonly http: HttpClient) { }

    listarUfs(): Observable<UfModel[]> {
        return this.http.get<UfModel[]>(`${AppConfig.endpoint}/dominio-siconv/ufs/`);
    }
}
