import { TestBed } from '@angular/core/testing';

import { ChamadosExternosService } from './chamados-externos.service';

describe('ChamadosExternosService', () => {
  let service: ChamadosExternosService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ChamadosExternosService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
