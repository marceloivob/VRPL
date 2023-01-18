import { SiconvModule } from './siconv.module';

describe('SiconvModule', () => {
  let siconvModule: SiconvModule;

  beforeEach(() => {
    siconvModule = new SiconvModule();
  });

  it('should create an instance', () => {
    expect(siconvModule).toBeTruthy();
  });
});
