package com.task.library.configuration;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;

//@Component("decider")
public class ReadCsvDecider  implements JobExecutionDecider {
    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        if (jobExecution.getJobParameters().getParameters().get("path") == null ||
                jobExecution.getJobParameters().getParameters().get("path").equals(""))
            return FlowExecutionStatus.STOPPED;
        else
            return FlowExecutionStatus.COMPLETED;
    }
}
