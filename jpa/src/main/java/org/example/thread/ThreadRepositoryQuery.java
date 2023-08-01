package org.example.thread;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ThreadRepositoryQuery {
    Page<Thread> search(ThreadSearchCond cond, Pageable pageable);
}
