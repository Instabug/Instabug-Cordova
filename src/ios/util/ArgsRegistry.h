#import <Foundation/Foundation.h>
#import <Instabug/IBGTypes.h>

typedef NSDictionary<NSString*, NSNumber*> ArgsDictionary;

@interface ArgsRegistry : NSObject

+ (ArgsDictionary *) recordButtonPositions;
+ (ArgsDictionary *) welcomeMessageModes;
+ (ArgsDictionary *) colorThemes;
+ (ArgsDictionary *) floatingButtonEdges;
+ (NSDictionary<NSString *, NSString *> *) placeholders;
+ (ArgsDictionary *) reproStepsModes;
+ (ArgsDictionary *) locales;

@end
