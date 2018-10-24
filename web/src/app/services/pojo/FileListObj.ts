import {BaseResponse} from './BaseResponse';

export class FileListBean {
  checked = false;
  filename: string;
  isFolder: boolean;
  size: number;
  permission: string;
  realPath: string;
}

export class FileListObj extends BaseResponse {
  obj: FileListBean[];
}

