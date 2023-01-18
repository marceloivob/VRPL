import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NaoExisteSPAHomologadoComponent } from './nao-existe-spa-homologado.component';

describe('NaoExisteSPAHomologadoComponent', () => {
  let component: NaoExisteSPAHomologadoComponent;
  let fixture: ComponentFixture<NaoExisteSPAHomologadoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [NaoExisteSPAHomologadoComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NaoExisteSPAHomologadoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
