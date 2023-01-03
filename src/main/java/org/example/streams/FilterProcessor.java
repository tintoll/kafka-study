package org.example.streams;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;

public class FilterProcessor implements Processor<String, String> {

    // 프로세스에 대한 정보를 담고 있다.
    private ProcessorContext context;

    @Override
    public void init(ProcessorContext context) {
        this.context = context;
    }

    @Override
    public void process(String key, String value) {
        // 실제 로직이 들어가는 부분
        if (value.length() > 5) {
            // forward 메서드를 사용하여 다음 토폴로지(다음 프로세서)로 넘어가도록 처리
            context.forward(key, value);
        }
        context.commit();
    }

    @Override
    public void close() {
    }
}
