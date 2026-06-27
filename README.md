graph TD
    subgraph “阶段一：预加载与缓存”
        A[车机系统启动完成] --> B{自动启动AVMService}
        B --> C[解析XML布局文件]
        C --> D[将View对象缓存到内存]
        D --> E[服务进入等待，监听触发信号]
    end

    subgraph “阶段二：瞬时显示”
        E --> F{监听到触发信号？<br/>（倒挡/按钮）}
        F -- 是 --> G[从缓存中取出预解析的View]
        G --> H[通过WindowManager.addView()<br/>创建悬浮窗]
        H --> I[用户看到全景画面<br/>（毫秒级响应）]
    end

    subgraph “阶段三：生命周期监听”
        I --> J[启动TransparentActivity]
        J --> K[TransparentActivity<br/>通过bindService绑定AVMService]
        K --> L{监听系统生命周期事件}
    end

    subgraph “阶段四：状态管理”
        L -- onPause --> M[通知AVMService暂停渲染]
        M --> N[释放GPU资源，保持连接]
        L -- onResume --> O[通知AVMService恢复渲染]
        O --> P[恢复GPU资源，画面恢复]
        L -- onDestroy --> Q[通知AVMService执行销毁]
        Q --> R[关闭摄像头，释放全部资源]
        R --> S[通过WindowManager.removeView()<br/>移除悬浮窗]
        S --> T[调用stopSelf()结束服务]
    end

    style A fill:#e1f5fe,stroke:#01579b
    style B fill:#e1f5fe,stroke:#01579b
    style C fill:#c8e6c9,stroke:#2e7d32
    style D fill:#c8e6c9,stroke:#2e7d32
    style E fill:#fff9c4,stroke:#fbc02d
    style F fill:#fff9c4,stroke:#fbc02d
    style G fill:#c8e6c9,stroke:#2e7d32
    style H fill:#e1f5fe,stroke:#01579b
    style I fill:#e1f5fe,stroke:#01579b
    style J fill:#bbdefb,stroke:#1565c0
    style K fill:#bbdefb,stroke:#1565c0
    style L fill:#bbdefb,stroke:#1565c0
    style M fill:#ffccbc,stroke:#bf360c
    style N fill:#ffccbc,stroke:#bf360c
    style O fill:#c8e6c9,stroke:#2e7d32
    style P fill:#c8e6c9,stroke:#2e7d32
    style Q fill:#ffccbc,stroke:#bf360c
    style R fill:#ffccbc,stroke:#bf360c
    style S fill:#ffccbc,stroke:#bf360c
    style T fill:#ffccbc,stroke:#bf360c
