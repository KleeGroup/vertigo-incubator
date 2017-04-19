/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2017, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.shell.commands;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import io.vertigo.app.Home;
import io.vertigo.core.component.ComponentInfo;
import io.vertigo.lang.Assertion;
import io.vertigo.shell.command.VCommand;
import io.vertigo.shell.command.VCommandExecutor;
import io.vertigo.util.ListBuilder;

/**
 * System Command Executor.
 * @author pchretien
 */
public final class VSystemCommandExecutor implements VCommandExecutor<List<ComponentInfo>> {
	private static Logger LOG = Logger.getLogger(VSystemCommandExecutor.class);

	/** {@inheritDoc} */
	@Override
	public List<ComponentInfo> exec(final VCommand command) {
		Assertion.checkNotNull(command);
		//Assertion.checkArgument(command.getName());
		//-----
		String address;
		try {
			address = InetAddress.getLocalHost().getHostAddress();
		} catch (final UnknownHostException e) {
			address = "????";
			LOG.warn("Unknown Host", e);
		}

		return new ListBuilder<ComponentInfo>()
				.add(new ComponentInfo("vertigo.start", new Date(Home.getApp().getStartDate())))
				.add(new ComponentInfo("vertigo.uptime#inseconds", (System.currentTimeMillis() - Home.getApp().getStartDate()) / 1000))

				//---
				.add(new ComponentInfo("java.version", System.getProperty("java.version")))
				.add(new ComponentInfo("os.name", System.getProperty("os.name")))
				.add(new ComponentInfo("os.version", System.getProperty("os.version")))
				//----
				.add(new ComponentInfo("host.address", address))
				//		componentInfos.add(new ComponentInfo(SystemManager.class, "log.level", Logger.getRootLogger().getLevel()));
				//----
				.add(new ComponentInfo("memory.free", Runtime.getRuntime().freeMemory()))
				.add(new ComponentInfo("memory.max", Runtime.getRuntime().maxMemory()))
				.add(new ComponentInfo("memory.totalMemory", Runtime.getRuntime().totalMemory()))
				//----
				.add(new ComponentInfo("thread.active", Thread.activeCount()))
				.build();
	}
}

//private final long lastThreadDumpTime = System.currentTimeMillis();
//private final Map<Long, Long> lastCpuTimeByThreadId = new HashMap<Long, Long>();

//private void doGC(final PrintStream out) {
//	Logger.getLogger(getClass()).info("Execution du garbage collector");
//	final long freeBegin = Runtime.getRuntime().freeMemory();
//	final long begin = System.currentTimeMillis();
//	System.gc();
//	final long end = System.currentTimeMillis();
//	long freeEnd = Runtime.getRuntime().freeMemory();
//	if (freeEnd < freeBegin) {
//		freeEnd = freeBegin;
//	}
//-----
//	// Rendu html de la page effectuant un garbage collector.
//	out.print("Garbage collector effectué en ");
//	out.print(SystemUtil.DOUBLE_FORMATTER.format(((double) end - begin) / 1000));
//	out.print(" s, mémoire libérée : ");
//	out.print(SystemUtil.DOUBLE_FORMATTER.format(((double) freeEnd - freeBegin) / 1024 / 1024));
//	out.print(" Mo");
//	out.println("<br/>");
//}

//private Map<Long, Long> getCpuTimeByThreadId() {
//	final Map<Long, Long> cpuTimeByThreadId = new HashMap<Long, Long>();
//	final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
//	for (final long threadId : threadBean.getAllThreadIds()) {
//		final long time = threadBean.getThreadCpuTime(threadId);
//		if (time != -1) {
//			cpuTimeByThreadId.put(threadId, time / 1000000);
//		}
//	}
//	return cpuTimeByThreadId;
//}
//
//private long getTotalCpuTime(final Map<Long, Long> currentCpuTimeByThreadId) {
//	long total = 0;
//	for (final Map.Entry<Long, Long> entry : currentCpuTimeByThreadId.entrySet()) {
//		if (lastCpuTimeByThreadId.containsKey(entry.getKey())) {
//			total += entry.getValue() - lastCpuTimeByThreadId.get(entry.getKey());
//		} else {
//			total += entry.getValue();
//		}
//	}
//	return total;
//}

//private synchronized void doThreadDump(final PrintStream out) {
//	//Synchronized car appellée pour l'affichage par un Thread de request HTTP : si double clic execution de cette méthode gourmande.
//	appendJavaScript(out);
//
//	final StringBuilder sb = new StringBuilder("Execution du ThreadDump à ");
//	sb.append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss:SSS").format(new Date()));
//
//	final Map<Long, Long> currentCpuTimeByThreadId = appendThreadsInformations(sb);
//
//	appendThreadsSummary(sb);
//
//	appendClassLoadingInformations(sb);
//
//	appendCompilationInformations(sb);
//
//	appendGarbageCollectionInformations(sb);
//
//	appendMemoryUsage(sb);
//
//	appendProcessInformations(sb);
//
//	//on passe le current en last.
//	lastCpuTimeByThreadId = currentCpuTimeByThreadId;
//	lastThreadDumpTime = System.currentTimeMillis();
//	//on rend dans la page
//	out.println(sb.toString());
//	//et dans le log
//	Logger.getLogger(getClass()).info(sb.toString());
//	//-----
//}

//private Map<Long, Long> appendThreadsInformations(final StringBuilder sb) {
//	sb.append("<br/>\nStackTraces : <br/>\n");
//
//	final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
//	final boolean cpuTime = threadBean.isThreadCpuTimeEnabled() && threadBean.isThreadCpuTimeSupported();
//	final Map<Long, Long> currentCpuTimeByThreadId;
//	final long totalCpuTime;
//	if (cpuTime) {
//		currentCpuTimeByThreadId = getCpuTimeByThreadId();
//		totalCpuTime = getTotalCpuTime(currentCpuTimeByThreadId);
//	} else {
//		currentCpuTimeByThreadId = lastCpuTimeByThreadId;
//		totalCpuTime = 0;
//	}
//	appendTotalCpuTime(sb, totalCpuTime);
//	int threadId = 0;
//	final Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
//	for (final Map.Entry<Thread, StackTraceElement[]> entry : map.entrySet()) {
//		threadId++;
//		final Thread thread = entry.getKey();
//		if (cpuTime) {
//			long cpuPercentil = 0;
//			long cpuDelta = 0;
//			if (currentCpuTimeByThreadId.containsKey(thread.getId()) && totalCpuTime > 0) {
//				cpuDelta = currentCpuTimeByThreadId.get(thread.getId());
//				if (lastCpuTimeByThreadId.containsKey(thread.getId())) {
//					cpuDelta -= lastCpuTimeByThreadId.get(thread.getId());
//				}
//				cpuPercentil = cpuDelta * 100 / totalCpuTime;
//				if (cpuPercentil >= 10) {
//					sb.append("<span style=\"background-color:");
//					sb.append(getColorByCpuPercentil(cpuPercentil));
//					sb.append("\"><b>");
//				}
//			}
//			appendThreadStackTraceOpener(sb, threadId);
//			appendThreadHeader(sb, thread);
//			sb.append(", cpuTime=").append(currentCpuTimeByThreadId.get(thread.getId())).append(" ms");
//			sb.append(", userTime=").append(threadBean.getThreadUserTime(thread.getId()) / 1000000).append(" ms");
//			sb.append(']');
//			sb.append(" cpu=").append(cpuDelta).append(" ms (").append(cpuPercentil).append("%)");
//			if (cpuPercentil >= 10) {
//				sb.append("</b></span>");
//			}
//			sb.append(" <i>sur ").append(totalCpuTime).append(" ms</i>");
//			sb.append("<div id=\"ST_").append(threadId).append("\" ");
//			if (cpuPercentil < 20) {
//				sb.append("STYLE=\"display:none;\"");
//			}
//			sb.append('>');
//		} else {
//			appendThreadStackTraceOpener(sb, threadId);
//			appendThreadHeader(sb, thread);
//			sb.append(']');
//			sb.append("<div id=\"ST_").append(threadId).append("\" STYLE=\"display:none;\">");
//		}
//		final StackTraceElement[] value = entry.getValue();
//		appendStackTrace(sb, value);
//		sb.append("</div><br/>\n");
//	}
//	return currentCpuTimeByThreadId;
//}

//private void appendThreadStackTraceOpener(final StringBuilder sb, final int threadId) {
//	sb.append("<a href=\"#\" onclick=\"openThreadStack('ST_").append(threadId).append("'); return false;\">Thread");
//	sb.append("</a>[");
//}
//
//private void appendThreadHeader(final StringBuilder sb, final Thread thread) {
//	sb.append("name=").append(thread.getName());
//	sb.append(", state=");
//	sb.append(thread.getState());
//	sb.append(", priority=");
//	sb.append(thread.getPriority());
//	if (thread.getThreadGroup() != null) {
//		sb.append(", threadGroup=").append(thread.getThreadGroup().getName());
//	}
//	if (thread.isDaemon()) {
//		sb.append(", daemon");
//	}
//}
//
//private void appendStackTrace(final StringBuilder sb, final StackTraceElement[] value) {
//	for (final StackTraceElement element : value) {
//		sb.append("&nbsp; &nbsp;").append(element).append("<br/>\n");
//	}
//}
//
//private void appendTotalCpuTime(final StringBuilder sb, final long totalCpuTime) {
//	final long currentTime = System.currentTimeMillis();
//	final long totalCpuPercentil = totalCpuTime * 100 / (currentTime - lastThreadDumpTime + 1); //+1 pour éviter le "/ by zero"
//	if (totalCpuPercentil >= 10) {
//		sb.append("<span style=\"background-color:");
//		sb.append(getColorByCpuPercentil(totalCpuPercentil));
//		sb.append("\"><b>");
//	}
//	sb.append("Total CPU time : ");
//	sb.append(totalCpuTime);
//	sb.append(" ms (");
//	sb.append(totalCpuPercentil);
//	sb.append("%) en ");
//	sb.append(currentTime - lastThreadDumpTime);
//	sb.append(" ms de temps réel.");
//	if (totalCpuPercentil >= 10) {
//		sb.append("</b></span>");
//	}
//	sb.append("<br/>\n");
//}
//
//private void appendThreadsSummary(final StringBuilder sb) {
//	final ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
//	sb.append("<br/>\nThreads : <br/>\n&nbsp; &nbsp; daemonThreadCount=");
//	sb.append(threadBean.getDaemonThreadCount());
//	sb.append(", threadCount=");
//	sb.append(threadBean.getThreadCount());
//	sb.append(", peakThreadCount=");
//	sb.append(threadBean.getPeakThreadCount());
//	sb.append(", totalStartedThreadCount=");
//	sb.append(threadBean.getTotalStartedThreadCount());
//}
//
//private void appendClassLoadingInformations(final StringBuilder sb) {
//	final ClassLoadingMXBean classLoading = ManagementFactory.getClassLoadingMXBean();
//	sb.append("<br/>\nClassLoading : <br/>\n&nbsp; &nbsp; loadedClassCount=");
//	sb.append(classLoading.getLoadedClassCount());
//	sb.append(", totalLoadedClassCount=");
//	sb.append(classLoading.getTotalLoadedClassCount());
//	sb.append(", unloadedClassCount=");
//	sb.append(classLoading.getUnloadedClassCount());
//	sb.append(", verbose=");
//	sb.append(classLoading.isVerbose());
//}
//
//private void appendGarbageCollectionInformations(final StringBuilder sb) {
//	for (final GarbageCollectorMXBean garbageCollector : ManagementFactory.getGarbageCollectorMXBeans()) {
//		sb.append("<br/>\nGarbageCollector : <br/>\n&nbsp; &nbsp; name=");
//		sb.append(garbageCollector.getName()).append(", collectionCount=");
//		sb.append(garbageCollector.getCollectionCount());
//		sb.append(", collectionTime=");
//		sb.append(garbageCollector.getCollectionTime());
//	}
//	for (final MemoryManagerMXBean memoryManager : ManagementFactory.getMemoryManagerMXBeans()) {
//		sb.append("<br/>\nMemoryManager : <br/>\n&nbsp; &nbsp; name=");
//		sb.append(memoryManager.getName()).append(", memoryPoolNames=");
//		sb.append(Arrays.asList(memoryManager.getMemoryPoolNames()));
//		sb.append(", valid=");
//		sb.append(memoryManager.isValid());
//	}
//}
//
//private void appendCompilationInformations(final StringBuilder sb) {
//	final CompilationMXBean compilation = ManagementFactory.getCompilationMXBean();
//	sb.append("<br/>\nCompilation : <br/>\n&nbsp; &nbsp;");
//	sb.append("name=");
//	sb.append(compilation.getName());
//	if (compilation.isCompilationTimeMonitoringSupported()) {
//		sb.append(", totalCompilationTime=").append(compilation.getTotalCompilationTime());
//	}
//}
//
//private void appendMemoryUsage(final StringBuilder sb) {
//	final MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
//	sb.append("<br/>\nMemory : <br/>\n&nbsp; &nbsp; objectPendingFinalizationCount=");
//	sb.append(memory.getObjectPendingFinalizationCount());
//	sb.append(", verbose=");
//	sb.append(memory.isVerbose());
//
//	final MemoryUsage heapMemoryUsage = memory.getHeapMemoryUsage();
//	sb.append("<br/>\nHeapMemoryUsage : <br/>\n&nbsp; &nbsp; init=");
//	sb.append(heapMemoryUsage.getInit() / 1024);
//	sb.append(" Ko, used=");
//	sb.append(heapMemoryUsage.getUsed() / 1024);
//	sb.append(" Ko, committed=");
//	sb.append(heapMemoryUsage.getCommitted() / 1024);
//	sb.append(" Ko, max=");
//	sb.append(heapMemoryUsage.getMax() / 1024);
//	sb.append(" Ko");
//	final MemoryUsage nonHeapMemoryUsage = memory.getNonHeapMemoryUsage();
//	sb.append("<br/>\nNonHeapMemoryUsage : <br/>\n&nbsp; &nbsp; init=");
//	sb.append(nonHeapMemoryUsage.getInit() / 1024);
//	sb.append(" Ko, used=");
//	sb.append(nonHeapMemoryUsage.getUsed() / 1024);
//	sb.append(" Ko, committed=");
//	sb.append(nonHeapMemoryUsage.getCommitted() / 1024);
//	sb.append(" Ko, max=");
//	sb.append(nonHeapMemoryUsage.getMax() / 1024);
//	sb.append(" Ko");
//}
//
//private void appendProcessInformations(final StringBuilder sb) {
//	final OperatingSystemMXBean operatingSystem = ManagementFactory.getOperatingSystemMXBean();
//	if ("com.sun.management.OperatingSystem".equals(operatingSystem.getClass().getName())) {
//		final com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) operatingSystem;
//		sb.append("<br/>\nOperatingSystem : <br/>\n&nbsp; &nbsp; committedVirtualMemorySize=");
//		sb.append(os.getCommittedVirtualMemorySize() / 1024 / 1024);
//		sb.append(" Mo, freePhysicalMemorySize=");
//		sb.append(os.getFreePhysicalMemorySize() / 1024 / 1024);
//		sb.append(" Mo");
//		sb.append(", freeSwapSpaceSize=");
//		sb.append(os.getFreeSwapSpaceSize() / 1024 / 1024);
//		sb.append(" Mo, processCpuTime=");
//		sb.append(os.getProcessCpuTime() / 1000000);
//		sb.append(" ms, totalPhysicalMemorySize=");
//		sb.append(os.getTotalPhysicalMemorySize() / 1024 / 1024);
//		sb.append(" Mo, totalSwapSpaceSize=");
//		sb.append(os.getTotalSwapSpaceSize() / 1024 / 1024);
//		sb.append(" Mo");
//	}
//}
//
//private String getColorByCpuPercentil(final long cpuPercentil) {
//	if (cpuPercentil >= 60) {
//		return "red";
//	} else if (cpuPercentil >= 40) {
//		return "Darkorange";
//	} else if (cpuPercentil >= 20) {
//		return "yellow";
//	} else if (cpuPercentil >= 10) {
//		return "Lime";
//	} else {
//		return "white";
//	}
//}

///** {@inheritDoc} */
//public ManagerListener getListener() {
//	return this;
//}

///** {@inheritDoc} */
//public void execute(final String actionName, final PrintStream out) {
//	if (ACTION_NAME_GC.equals(actionName)) {
//		doGC(out);
//	} else if (ACTION_NAME_THREADDUMP.equals(actionName)) {
//		doThreadDump(out);
//	} else {
//		out.print("Action ");
//		out.print(actionName);
//		out.print(" inconnue");
//	}
//}
//
//private void appendJavaScript(final PrintStream out) {
//	out.println("\n<script language=\"JavaScript\">");
//	out.println("\n<!--");
//	out.println("\nfunction openThreadStack(myElementId) {");
//	out.println("\n	var isVisible = document.getElementById(myElementId).style.display == \"block\";");
//	out.println("\n	if (document.getElementById) {");
//	out.println("\n		if(!isVisible)");
//	out.println("\n			document.getElementById(myElementId).style.display = \"block\";");
//	out.println("\n		else");
//	out.println("\n    		document.getElementById(myElementId).style.display = \"none\";");
//	out.println("\n    	isVisible = !isVisible;");
//	out.println("\n  	} else {");
//	out.println("\n    	document.write(\"Unrecognized Browser Detected\");");
//	out.println("\n  	}");
//	out.println("\n}");
//	out.println("\n//-->");
//	out.println("\n</script>");
//}
