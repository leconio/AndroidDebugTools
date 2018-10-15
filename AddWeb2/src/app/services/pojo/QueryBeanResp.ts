import {BaseResponse} from './BaseResponse';

export class QueryBeanResp extends BaseResponse {
  obj: QueryBean;
}

export class QueryBean {
  list: any[];
  columns: string[];
  pageInfo: PageInfo;
}

export class PageInfo {
  count: number;
}
