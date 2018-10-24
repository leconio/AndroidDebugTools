import {BaseResponse} from './BaseResponse';

export class QueryBeanResp extends BaseResponse {
  obj: QueryBean;
}

export class QueryBean {
  list: any[];
  columns: ColumnsInfo[];
  pageInfo: PageInfo;
}

export class ColumnsInfo {
  columnName: string;
  isPrimary: boolean;
}

export class PageInfo {
  count: number;
}
