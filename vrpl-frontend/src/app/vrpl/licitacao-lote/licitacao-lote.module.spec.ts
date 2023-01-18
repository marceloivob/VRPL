import { LicitacaoLoteModule } from './licitacao-lote.module';

describe('LicitacaoLoteModule', () => {
  let licitacaoLoteModule: LicitacaoLoteModule;

  beforeEach(() => {
    licitacaoLoteModule = new LicitacaoLoteModule();
  });

  it('should create an instance', () => {
    expect(licitacaoLoteModule).toBeTruthy();
  });
});
