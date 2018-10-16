import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiskContentComponent } from './disk-content.component';

describe('DiskContentComponent', () => {
  let component: DiskContentComponent;
  let fixture: ComponentFixture<DiskContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiskContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiskContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
