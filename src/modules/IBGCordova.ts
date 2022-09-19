export const exec = (
  service: string,
  action: string,
  args?: any[],
  success?: (data: any) => any,
  fail?: (err: any) => any
) => {
  cordova.exec(
    success!,
    fail!,
    service,
    action,
    args
  );
};
