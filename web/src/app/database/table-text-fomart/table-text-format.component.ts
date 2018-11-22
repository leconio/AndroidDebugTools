import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'app-table-text-format',
  templateUrl: './table-text-format.component.html',
  styleUrls: ['./table-text-format.component.css']
})
export class TableTextFormatComponent implements OnInit {

  @Input()
  text: string;

  constructor() {
  }

  ngOnInit() {
  }

}
