interface ListResult<T> {
  items: {
    item: T[];
  };
  limitExceeded: boolean;
  size: number;
  type: string;
}