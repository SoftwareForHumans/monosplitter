import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DecompositionDetailsComponent } from './decomposition-details.component';

describe('DecompositionDetailsComponent', () => {
  let component: DecompositionDetailsComponent;
  let fixture: ComponentFixture<DecompositionDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DecompositionDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DecompositionDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
