import {Component, Input, OnInit, Output} from '@angular/core';
import {NzMessageService} from 'ng-zorro-antd';
import {Urls} from '../../services/api-services.service';

@Component({
  selector: 'app-table-text-format',
  templateUrl: './table-text-format.component.html',
  styleUrls: ['./table-text-format.component.css']
})
export class TableTextFormatComponent implements OnInit {

  @Input()
  @Output()
  text: string;

  constructor(private message: NzMessageService) {
  }

  ngOnInit() {
  }

  successFun() {
    this.message.success('复制成功', {nzDuration: 3000});
  }

  parseJson(json: string) {
    window.localStorage.setItem('json', json);
    window.open(Urls.baseUrl + 'assets/json/json.html');
  }

  isJson(text: string) {
    return text.startsWith('{') && text.endsWith('}');
  }

  openNew(text: string) {
    window.localStorage.setItem('content', text);
    window.open(Urls.baseUrl + 'assets/empty.html');
  }
}
