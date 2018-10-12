import {BaseResponse} from './BaseResponse';

export class DatabaseListObj extends BaseResponse {
    obj: DatabaseObj[];
}

export class DatabaseObj {
    name: string;
    path: string;
}
