import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LockedOrdersComponent } from './locked-orders.component';

describe('LockedOrdersComponent', () => {
  let component: LockedOrdersComponent;
  let fixture: ComponentFixture<LockedOrdersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LockedOrdersComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LockedOrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
