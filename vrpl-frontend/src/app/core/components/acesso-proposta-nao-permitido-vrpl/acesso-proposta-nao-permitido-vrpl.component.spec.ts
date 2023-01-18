/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AcessoPropostaNaoPermitidoVrplComponent } from './acesso-proposta-nao-permitido-vrpl.component';

describe('AcessoPropostaNaoPermitidoVrplComponent', () => {
  let component: AcessoPropostaNaoPermitidoVrplComponent;
  let fixture: ComponentFixture<AcessoPropostaNaoPermitidoVrplComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AcessoPropostaNaoPermitidoVrplComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AcessoPropostaNaoPermitidoVrplComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
