import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {ApiServices, Urls} from '../../services/api-services.service';
import {FileListBean} from '../../services/pojo/FileListObj';
import {NzModalService} from 'ng-zorro-antd';

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
  allChecked = false;
  indeterminate = false;
  displayData = [];
  dataSet: FileListBean[];

  folder = [''];

  constructor(private route: ActivatedRoute, private apiServices: ApiServices, private modalService: NzModalService) {
  }

  ngOnInit() {
    this.route.params.subscribe((params) => {
      this.type = params['type'];
      this.folder = [];
      this.getFileList(this.folder.join('/'));
    });
  }

  getFileList(path: string) {
    this.apiServices.getFileList(this.type, path)
      .subscribe((fileList) => {
        this.dataSet = fileList.obj.sort((left: FileListBean, right: FileListBean) => {
          if (left.isFolder && right.isFolder) {
            return -1;
          } else {
            return 1;
          }
        });
        this.refreshStatus();
      });
  }

  currentPageDataChange($event: Array<{ name: string; age: number; address: string; checked: boolean; disabled: boolean; }>): void {
    this.displayData = $event;
    this.refreshStatus();
  }

  refreshStatus(): void {
    const allChecked = this.displayData.filter(value => !value.disabled).every(value => value.checked === true);
    const allUnChecked = this.displayData.filter(value => !value.disabled).every(value => !value.checked);
    this.allChecked = allChecked;
    this.indeterminate = (!allChecked) && (!allUnChecked);
  }

  checkAll(value: boolean): void {
    this.displayData.forEach(data => {
      data.checked = value;
    });
    this.refreshStatus();
  }

  nextLevel(name: string, isFolder: boolean) {
    if (isFolder) {
      this.folder.push(name);
      this.getFileList(this.folder.join('/'));
    }
  }

  folderClick(index) {
    if ((index + 1) !== this.folder.length) {
      this.folder = this.folder.slice(0, index + 1);
      this.getFileList(this.folder.join('/'));
    }
  }

  rootClick() {
    this.folder = [];
    this.getFileList(this.folder.join('/'));
  }

  sureDel(path) {
    console.log(path);
  }

  download(path: string) {
    this.modalService.info({
      nzTitle: '确定下载？',
      nzContent: '<p>手机性能有限，文件需压缩后下载，下载可能需要一点时间</p><p style="color: red">点击确定后可能无法进行任何操作，耐心等待</p>',
      nzOnOk: () => this.apiServices.downloadFile(path),
      nzCancelText: '取消',
    });
  }

  bytesToSize(bytes: number) {
    if (bytes === 0) {
      return '0 B';
    }
    const k = 1000; // or 1024
    const sizes = ['B', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));

    return (bytes / Math.pow(k, i)).toPrecision(3) + ' ' + sizes[i];
  }
}
