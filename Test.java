import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicReference;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

public class Test {

  private static final int LOOP_COUNT = 20000;

  public static void main(String[] args) throws InterruptedException {
    AtomicReference<Nmt> peakNmtRef = new AtomicReference<>();
    Thread thread = new Thread(() -> updatePeakNmt(peakNmtRef));
    thread.setDaemon(true);
    thread.start();
    UnmarshallerContext context = new UnmarshallerContext();
    for (int i = 0; i < LOOP_COUNT; i++) {
      DescribeDBInstanceAttributeResponse response = new DescribeDBInstanceAttributeResponse();
      response.unmarshall(context);
      Thread.sleep(1);
    }
    thread.interrupt();
    thread.join();
    dumpPeakNmt(peakNmtRef.get());
  }

  private static void updatePeakNmt(AtomicReference<Nmt> peakNmtRef) {
    while (true) {
      Nmt nmt = Nmt.get();
      Nmt peakNmt = peakNmtRef.get();
      if (peakNmt == null || nmt.categories.get("total").committed > peakNmt.categories.get("total").committed) {
        peakNmtRef.set(nmt);
      }
      try {
        Thread.sleep(0, 500*1000);
      } catch (InterruptedException e) {
        break;
      }
    }
  }

  private static void dumpPeakNmt(Nmt nmt) {
    System.out.printf("peak total committed happened at %d ms, done at %d ms\n",
        nmt.timestamp / 1000_000, (System.nanoTime() - Nmt.startAt) / 1000_000);
    nmt.categories.forEach((k, v) -> {
      long mb = v.committed / Nmt.K / Nmt.K;
      if (mb > 0) {
        System.out.printf("%s: %d MB\n", k, mb);
      }
    });
  }

  static class UnmarshallerContext {

    public String stringValue(String key) {
      return "string" + ThreadLocalRandom.current().nextInt(20, 100);
    }

    public Integer integerValue(String key) {
      return ThreadLocalRandom.current().nextInt(20, 100);
    }

    public int lengthValue(String key) {
      return ThreadLocalRandom.current().nextInt(1, 20);
    }

    public Boolean booleanValue(String key) {
      return true;
    }

    public Long longValue(String key) {
      return 123L;
    }
  }

  static class DescribeDBInstanceAttributeResponse {
    private String requestId;
    private List<DBInstanceAttribute> items;

    public void unmarshall(UnmarshallerContext _ctx) {
      this.requestId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.RequestId");
      List<DBInstanceAttribute> items = new ArrayList<>();

      for(int i = 0; i < _ctx.lengthValue("DescribeDBInstanceAttributeResponse.Items.Length"); ++i) {
        DescribeDBInstanceAttributeResponse.DBInstanceAttribute dBInstanceAttribute = new DescribeDBInstanceAttributeResponse.DBInstanceAttribute();
        dBInstanceAttribute.iPType = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].IPType");
        dBInstanceAttribute.dBInstanceDiskUsed = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceDiskUsed");
        dBInstanceAttribute.guardDBInstanceName = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].GuardDBInstanceName");
        dBInstanceAttribute.canTempUpgrade = _ctx.booleanValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].CanTempUpgrade");
        dBInstanceAttribute.tempUpgradeTimeStart = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TempUpgradeTimeStart");
        dBInstanceAttribute.tempUpgradeTimeEnd = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TempUpgradeTimeEnd");
        dBInstanceAttribute.tempUpgradeRecoveryTime = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TempUpgradeRecoveryTime");
        dBInstanceAttribute.tempUpgradeRecoveryClass = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TempUpgradeRecoveryClass");
        dBInstanceAttribute.tempUpgradeRecoveryCpu = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TempUpgradeRecoveryCpu");
        dBInstanceAttribute.tempUpgradeRecoveryMemory = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TempUpgradeRecoveryMemory");
        dBInstanceAttribute.tempUpgradeRecoveryMaxIOPS = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TempUpgradeRecoveryMaxIOPS");
        dBInstanceAttribute.tempUpgradeRecoveryMaxConnections = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TempUpgradeRecoveryMaxConnections");
        dBInstanceAttribute.insId = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].InsId");
        dBInstanceAttribute.dBInstanceId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceId");
        dBInstanceAttribute.payType = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].PayType");
        dBInstanceAttribute.dBInstanceClassType = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceClassType");
        dBInstanceAttribute.dBInstanceType = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceType");
        dBInstanceAttribute.regionId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].RegionId");
        dBInstanceAttribute.connectionString = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ConnectionString");
        dBInstanceAttribute.port = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].Port");
        dBInstanceAttribute.engine = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].Engine");
        dBInstanceAttribute.engineVersion = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].EngineVersion");
        dBInstanceAttribute.dBInstanceClass = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceClass");
        dBInstanceAttribute.dBInstanceMemory = _ctx.longValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceMemory");
        dBInstanceAttribute.dBInstanceStorage = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceStorage");
        dBInstanceAttribute.vpcCloudInstanceId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].VpcCloudInstanceId");
        dBInstanceAttribute.dBInstanceNetType = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceNetType");
        dBInstanceAttribute.dBInstanceStatus = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceStatus");
        dBInstanceAttribute.dBInstanceDescription = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceDescription");
        dBInstanceAttribute.lockMode = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].LockMode");
        dBInstanceAttribute.lockReason = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].LockReason");
        dBInstanceAttribute.readDelayTime = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ReadDelayTime");
        dBInstanceAttribute.dBMaxQuantity = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBMaxQuantity");
        dBInstanceAttribute.accountMaxQuantity = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].AccountMaxQuantity");
        dBInstanceAttribute.creationTime = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].CreationTime");
        dBInstanceAttribute.expireTime = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ExpireTime");
        dBInstanceAttribute.maintainTime = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].MaintainTime");
        dBInstanceAttribute.availabilityValue = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].AvailabilityValue");
        dBInstanceAttribute.maxIOPS = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].MaxIOPS");
        dBInstanceAttribute.maxConnections = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].MaxConnections");
        dBInstanceAttribute.masterInstanceId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].MasterInstanceId");
        dBInstanceAttribute.dBInstanceCPU = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceCPU");
        dBInstanceAttribute.incrementSourceDBInstanceId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].IncrementSourceDBInstanceId");
        dBInstanceAttribute.guardDBInstanceId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].GuardDBInstanceId");
        dBInstanceAttribute.replicateId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ReplicateId");
        dBInstanceAttribute.tempDBInstanceId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TempDBInstanceId");
        dBInstanceAttribute.securityIPList = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].SecurityIPList");
        dBInstanceAttribute.zoneId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ZoneId");
        dBInstanceAttribute.instanceNetworkType = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].InstanceNetworkType");
        dBInstanceAttribute.dBInstanceStorageType = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DBInstanceStorageType");
        dBInstanceAttribute.advancedFeatures = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].AdvancedFeatures");
        dBInstanceAttribute.category = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].Category");
        dBInstanceAttribute.accountType = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].AccountType");
        dBInstanceAttribute.supportUpgradeAccountType = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].SupportUpgradeAccountType");
        dBInstanceAttribute.supportCreateSuperAccount = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].SupportCreateSuperAccount");
        dBInstanceAttribute.vpcId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].VpcId");
        dBInstanceAttribute.vSwitchId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].VSwitchId");
        dBInstanceAttribute.connectionMode = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ConnectionMode");
        dBInstanceAttribute.currentKernelVersion = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].CurrentKernelVersion");
        dBInstanceAttribute.latestKernelVersion = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].LatestKernelVersion");
        dBInstanceAttribute.resourceGroupId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ResourceGroupId");
        dBInstanceAttribute.readonlyInstanceSQLDelayedTime = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ReadonlyInstanceSQLDelayedTime");
        dBInstanceAttribute.securityIPMode = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].SecurityIPMode");
        dBInstanceAttribute.timeZone = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TimeZone");
        dBInstanceAttribute.collation = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].Collation");
        dBInstanceAttribute.dispenseMode = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DispenseMode");
        dBInstanceAttribute.masterZone = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].MasterZone");
        dBInstanceAttribute.autoUpgradeMinorVersion = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].AutoUpgradeMinorVersion");
        dBInstanceAttribute.proxyType = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ProxyType");
        dBInstanceAttribute.consoleVersion = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].ConsoleVersion");
        dBInstanceAttribute.multipleTempUpgrade = _ctx.booleanValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].MultipleTempUpgrade");
        dBInstanceAttribute.originConfiguration = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].OriginConfiguration");
        dBInstanceAttribute.dedicatedHostGroupId = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].DedicatedHostGroupId");
        dBInstanceAttribute.superPermissionMode = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].SuperPermissionMode");
        dBInstanceAttribute.generalGroupName = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].GeneralGroupName");
        dBInstanceAttribute.tipsLevel = _ctx.integerValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].TipsLevel");
        dBInstanceAttribute.tips = _ctx.stringValue("DescribeDBInstanceAttributeResponse.Items[" + i + "].Tips");
        items.add(dBInstanceAttribute);
      }

      this.items = items;
    }

    public static class DBInstanceAttribute {
      private String iPType;
      private String dBInstanceDiskUsed;
      private String guardDBInstanceName;
      private Boolean canTempUpgrade;
      private String tempUpgradeTimeStart;
      private String tempUpgradeTimeEnd;
      private String tempUpgradeRecoveryTime;
      private String tempUpgradeRecoveryClass;
      private Integer tempUpgradeRecoveryCpu;
      private Integer tempUpgradeRecoveryMemory;
      private String tempUpgradeRecoveryMaxIOPS;
      private String tempUpgradeRecoveryMaxConnections;
      private Integer insId;
      private String dBInstanceId;
      private String payType;
      private String dBInstanceClassType;
      private String dBInstanceType;
      private String regionId;
      private String connectionString;
      private String port;
      private String engine;
      private String engineVersion;
      private String dBInstanceClass;
      private Long dBInstanceMemory;
      private Integer dBInstanceStorage;
      private String vpcCloudInstanceId;
      private String dBInstanceNetType;
      private String dBInstanceStatus;
      private String dBInstanceDescription;
      private String lockMode;
      private String lockReason;
      private String readDelayTime;
      private Integer dBMaxQuantity;
      private Integer accountMaxQuantity;
      private String creationTime;
      private String expireTime;
      private String maintainTime;
      private String availabilityValue;
      private Integer maxIOPS;
      private Integer maxConnections;
      private String masterInstanceId;
      private String dBInstanceCPU;
      private String incrementSourceDBInstanceId;
      private String guardDBInstanceId;
      private String replicateId;
      private String tempDBInstanceId;
      private String securityIPList;
      private String zoneId;
      private String instanceNetworkType;
      private String dBInstanceStorageType;
      private String advancedFeatures;
      private String category;
      private String accountType;
      private String supportUpgradeAccountType;
      private String supportCreateSuperAccount;
      private String vpcId;
      private String vSwitchId;
      private String connectionMode;
      private String currentKernelVersion;
      private String latestKernelVersion;
      private String resourceGroupId;
      private String readonlyInstanceSQLDelayedTime;
      private String securityIPMode;
      private String timeZone;
      private String collation;
      private String dispenseMode;
      private String masterZone;
      private String autoUpgradeMinorVersion;
      private Integer proxyType;
      private String consoleVersion;
      private Boolean multipleTempUpgrade;
      private String originConfiguration;
      private String dedicatedHostGroupId;
      private String superPermissionMode;
      private String generalGroupName;
      private Integer tipsLevel;
      private String tips;
    }
  }


  static class Nmt {
    static final long K = 1024;
    static final long startAt = System.nanoTime();

    public final Map<String, Usage> categories;
    public final long timestamp = System.nanoTime() - startAt;

    private Nmt(final Map<String, Usage> categories) {
      this.categories = categories;
    }

    private static String invokeMxCommand() {
      String cmd = "vmNativeMemory";
      String[] arguments = {"summary"};
      final ObjectName name;
      try {
        name = new ObjectName("com.sun.management", "type", "DiagnosticCommand");
      } catch (MalformedObjectNameException e) {
        throw new AssertionError("should never happen", e);
      }

      final MBeanServer server = ManagementFactory.getPlatformMBeanServer();

      final Object[] wrappedArgs = {arguments};
      final String[] signature = {String[].class.getName()};
      try {
        return (String)server.invoke(name, cmd, wrappedArgs, signature);
      } catch (InstanceNotFoundException | MBeanException | ReflectionException exception) {
        exception.printStackTrace();
      }
      return null;
    }

    public static Nmt get() {
      final String nmt = invokeMxCommand();
      if (nmt == null) {
        return null;
      }
      try {
        return parse(nmt);
      } catch (IllegalArgumentException exception) {
        exception.printStackTrace();;
        return null;
      }
    }

    static Usage parseUsage(final String line) {
      // "-                  Compiler (reserved=229KB, committed=229KB)"
      final String firstKB = "KB, ";
      final int reservedUnitIndex;
      final int reservedLabelIndex;
      final long reserved;
      final long committed;

      // parse reserved column
      {
        final String reservedLabel = "reserved=";
        reservedLabelIndex = line.indexOf(reservedLabel);
        if (reservedLabelIndex == -1) {
          throw new IllegalArgumentException("could not find reserved label");
        }
        reservedUnitIndex = line.indexOf(firstKB, reservedLabelIndex);
        if (reservedUnitIndex == -1) {
          throw new IllegalArgumentException("could not find KB after reserved label");
        }
        final String reservedStr = line.substring(reservedLabelIndex + reservedLabel.length(), reservedUnitIndex);
        try {
          reserved = Long.parseLong(reservedStr) * K;
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException(String.format("could not parse reserved %s", reservedStr), e);
        }
      }

      // parse committed column
      {
        final String committedLabel = "committed=";
        final int committedLabelIndex = line.indexOf(committedLabel, reservedUnitIndex + firstKB.length());
        if (committedLabelIndex == -1) {
          throw new IllegalArgumentException("could not find committed label");
        }
        final int committedUnitIndex = line.indexOf("KB", committedLabelIndex);
        final String committedStr = line.substring(committedLabelIndex + committedLabel.length(), committedUnitIndex);
        try {
          committed = Long.parseLong(committedStr) * K;
        } catch (NumberFormatException e) {
          throw new IllegalArgumentException(String.format("could not parse committed %s", committedStr), e);
        }
      }

      return new Usage(reserved, committed);
    }

    static Nmt parse(final String nmt) {
      final String prefixTotal = "Total: ";
      final String prefixDash = "-";
      final String prefixParen = " (";

      final List<String> lines = Arrays.asList(nmt.split("\n"));
      final Iterator<String> itr = lines.iterator();
      int lineCount = 0;

      String totalStr = null;
      while (itr.hasNext()) {
        String line = itr.next();
        ++lineCount;
        if (line.startsWith(prefixTotal)) {
          totalStr = line;
          break;
        }
      }

      if (totalStr == null || !totalStr.startsWith(prefixTotal)) {
        throw new IllegalArgumentException("total line not found");
      }

      final Usage total;
      try {
        total = parseUsage(totalStr.substring(prefixTotal.length()));
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException("could not parse total", e);
      }
      final Map<String, Usage> categories = new LinkedHashMap<>();
      categories.put("total", total);
      while (itr.hasNext()) {
        String line = itr.next();
        ++lineCount;
        if (!line.startsWith(prefixDash)) {
          continue;
        }
        line = line.substring(prefixDash.length()).trim();
        final int leftParenIndex = line.indexOf(prefixParen);
        if (leftParenIndex == -1) {
          throw new IllegalArgumentException(String.format("missing opening paren on line %d", lineCount));
        }
        final String category = line.substring(0, leftParenIndex);
        final Usage usage;
        try {
          usage = parseUsage(line.substring(leftParenIndex + prefixParen.length()));
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException(String.format("could not parse usage on line %d", lineCount), e);
        }
        categories.put(category, usage);
      }
      if (categories.isEmpty()) {
        throw new IllegalArgumentException("no categories parsed");
      }
      return new Nmt(categories);
    }

    /**
     * Fields are in bytes.
     */
    public static class Usage {
      public final long reserved;
      public final long committed;

      public Usage(final long reserved, final long committed) {
        this.reserved = reserved;
        this.committed = committed;
      }
    }
  }
}
