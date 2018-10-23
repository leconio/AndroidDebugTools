import {BaseResponse} from './BaseResponse';

export class VersionObj extends BaseResponse {
  obj: Version;
}

class Version {
  version: string;
}
