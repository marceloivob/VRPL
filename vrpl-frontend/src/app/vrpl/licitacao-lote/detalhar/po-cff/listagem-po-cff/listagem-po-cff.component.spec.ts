import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListagemPoCffComponent } from './listagem-po-cff.component';

describe('ListagemPoCffComponent', () => {
  let component: ListagemPoCffComponent;
  let fixture: ComponentFixture<ListagemPoCffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListagemPoCffComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListagemPoCffComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*it('should create', () => {
    expect(component).toBeTruthy();
  });*/
});
