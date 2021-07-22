import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MonolithClustersTextualComponent } from './monolith-clusters-textual.component';

describe('MonolithClustersTextualComponent', () => {
  let component: MonolithClustersTextualComponent;
  let fixture: ComponentFixture<MonolithClustersTextualComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MonolithClustersTextualComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MonolithClustersTextualComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
