import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DiskSiderComponent } from './disk-sider.component';

describe('DiskSiderComponent', () => {
  let component: DiskSiderComponent;
  let fixture: ComponentFixture<DiskSiderComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DiskSiderComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DiskSiderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
