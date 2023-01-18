import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroCffComEventosComponent } from './cadastro-cff-com-eventos.component';

describe('CadastroCffComEventosComponent', () => {
  let component: CadastroCffComEventosComponent;
  let fixture: ComponentFixture<CadastroCffComEventosComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CadastroCffComEventosComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CadastroCffComEventosComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // it('should create', () => {
  //   expect(component).toBeTruthy();
  // });
});
