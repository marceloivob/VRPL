import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListagemQciComponent } from './listagem-qci.component';

describe('ListagemQciComponent', () => {
  let component: ListagemQciComponent;
  let fixture: ComponentFixture<ListagemQciComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListagemQciComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListagemQciComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  /*
  it('should create', () => {
    expect(component).toBeTruthy();
  });
  */
});
