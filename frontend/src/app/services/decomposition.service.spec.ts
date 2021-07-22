import { TestBed } from '@angular/core/testing';

import { DecompositionService } from './decomposition.service';

describe('DecompositionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DecompositionService = TestBed.get(DecompositionService);
    expect(service).toBeTruthy();
  });
});
