#import <Foundation/Foundation.h>
#import <Instabug/IBGTypes.h>

typedef NSDictionary<NSString*, NSNumber*> ArgsDictionary;

@interface ArgsRegistry : NSObject

+ (ArgsDictionary *) floatingButtonEdges;
+ (NSDictionary<NSString *, NSString *> *) placeholders;

@end
