import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DecompositionResultComponent } from './decomposition-result.component';

describe('DecompositionResultComponent', () => {
  let component: DecompositionResultComponent;
  let fixture: ComponentFixture<DecompositionResultComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DecompositionResultComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecompositionResultComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
