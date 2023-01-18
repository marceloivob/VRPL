import { EditarPoCffModule } from './editar-po-cff.module';

describe('EditarPoCffModule', () => {
  let editarPoCffModule: EditarPoCffModule;

  beforeEach(() => {
    editarPoCffModule = new EditarPoCffModule();
  });

  it('should create an instance', () => {
    expect(editarPoCffModule).toBeTruthy();
  });
});
