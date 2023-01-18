import { DetalharModule } from './detalhar.module';

describe('DetalharModule', () => {
  let detalharModule: DetalharModule;

  beforeEach(() => {
    detalharModule = new DetalharModule();
  });

  it('should create an instance', () => {
    expect(detalharModule).toBeTruthy();
  });
});
