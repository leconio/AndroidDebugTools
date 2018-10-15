import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SiderItemMenuComponent } from './sider-item-menu.component';

describe('SiderItemMenuComponent', () => {
  let component: SiderItemMenuComponent;
  let fixture: ComponentFixture<SiderItemMenuComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SiderItemMenuComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SiderItemMenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
