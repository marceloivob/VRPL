import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { PropostaSemDadosImportadosComponent } from './proposta-sem-dados-importados.component';

describe('PropostaSemDadosImportadosComponent', () => {
  let component: PropostaSemDadosImportadosComponent;
  let fixture: ComponentFixture<PropostaSemDadosImportadosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ PropostaSemDadosImportadosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(PropostaSemDadosImportadosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
