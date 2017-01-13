export default class ListItem implements IListItem {
  constructor(private data?: any) {
    angular.extend(this, (data || {}));
    delete this.data;
  }

  title: string;
  iconClass: string;
  href: string;
}
