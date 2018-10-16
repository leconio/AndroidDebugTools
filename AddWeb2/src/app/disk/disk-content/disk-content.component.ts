import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ApiServices} from '../../services/api-services.service';

@Component({
  selector: 'app-disk-content',
  templateUrl: './disk-content.component.html',
  styleUrls: ['./disk-content.component.css']
})
export class DiskContentComponent implements OnInit {

  /**
   * 存储器类型 内部和外部
   */
  type: string;

  constructor(private route: ActivatedRoute, private apiServices: ApiServices) {
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.type = params['type'];
    });
  }

}
