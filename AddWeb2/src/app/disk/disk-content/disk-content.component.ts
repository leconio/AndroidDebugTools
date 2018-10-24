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

  allChecked = false;
  indeterminate = false;
  displayData = [];
  dataSet: FileListBean[];

  folder = [''];

  isVisible = false;
  isConfirmLoading = false;
  isLoading: boolean;

  /**
   * 存储器类型 内部和外部
   */
  private type: string;
  private downloadPath: string;
  private isFolder: boolean;

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
    this.isLoading = true;
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
    this.isLoading = false;
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

  download(isFolder: boolean, path: string) {
    this.isFolder = isFolder;
    this.downloadPath = path;
    this.isVisible = true;
  }

  handleDownloadOk(): void {
    this.isConfirmLoading = true;
    this.apiServices.downloadFile(this.isFolder, this.downloadPath, () => {
      this.isConfirmLoading = false;
      this.isVisible = false;
    });
  }

  handleCancel(): void {
    this.isVisible = false;
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
