import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableTextFormatComponent } from './table-text-format.component';

describe('TableTextFormatComponent', () => {
  let component: TableTextFormatComponent;
  let fixture: ComponentFixture<TableTextFormatComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableTextFormatComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableTextFormatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
